package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.compress.ArchiveType;
import gov.hhs.onc.dcdt.compress.utils.ArchiveUtils;
import gov.hhs.onc.dcdt.compress.utils.ArchiveUtils.ZipArchiveEntryPairTransformer;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map.Entry;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("adminController")
@DisplayController
public class AdminController extends AbstractToolController {
    private final static String FILE_NAME_SUFFIX_CREDS_ARCHIVE = "_creds";
    private final static String FILE_NAME_SUFFIX_CRED_KEY = "_key";

    @RequestMapping(value = { "/admin/instance/creds" }, method = { RequestMethod.GET })
    public void downloadInstanceCredentials(HttpServletResponse servletResp) throws ToolWebException {
        InstanceConfig instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.appContext, InstanceConfig.class);

        // noinspection ConstantConditions
        if (!instanceConfig.isConfigured()) {
            throw new ToolWebException("Instance configuration is not set.");
        }

        // noinspection ConstantConditions
        List<DiscoveryTestcaseCredential> creds =
            Stream
                .concat(
                    ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class).stream()
                        .flatMap(discoveryTestcase -> (discoveryTestcase.hasCredentials() ? discoveryTestcase.getCredentials().stream() : Stream.empty())),
                    ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcaseCredential.class).stream()
                        .filter(discoveryTestcaseCred -> discoveryTestcaseCred.getType().isCa())).filter(DiscoveryTestcaseCredential::hasCredentialInfo)
                .collect(Collectors.toList());

        List<Entry<String, byte[]>> credEntryDescPairs = new ArrayList<>(creds.size() * 3);
        String credName;
        CredentialInfo credInfo;
        KeyInfo keyInfo;
        CertificateInfo certInfo;
        X509Certificate cert;

        for (DiscoveryTestcaseCredential cred : creds) {
            credName = cred.getName();

            // noinspection ConstantConditions
            if ((credInfo = cred.getCredentialInfo()).hasKeyDescriptor() && (keyInfo = credInfo.getKeyDescriptor()).hasPrivateKey()) {
                for (DataEncoding dataEnc : DataEncoding.values()) {
                    try {
                        credEntryDescPairs.add(new ImmutablePair<>((credName + FILE_NAME_SUFFIX_CRED_KEY + dataEnc.getFileExtension()), KeyUtils.writeKey(
                            keyInfo.getPrivateKey(), dataEnc)));
                    } catch (CryptographyException e) {
                        throw new ToolWebException(String.format("Unable to write Discovery credential (name=%s) private key data.", credName), e);
                    }
                }
            }

            // noinspection ConstantConditions
            if ((certInfo = credInfo.getCertificateDescriptor()).hasCertificate()) {
                cert = certInfo.getCertificate();

                for (DataEncoding dataEnc : DataEncoding.values()) {
                    try {
                        credEntryDescPairs.add(new ImmutablePair<>((credName + dataEnc.getFileExtension()), CertificateUtils.writeCertificate(
                            certInfo.getCertificate(), dataEnc)));
                    } catch (CryptographyException e) {
                        // noinspection ConstantConditions
                        throw new ToolWebException(String.format(
                            "Unable to write Discovery credential (name=%s) certificate (subjDn={%s}, serialNum=%s, issuerDn={%s}) data.", credName,
                            cert.getSubjectDN(), certInfo.getSerialNumber(), cert.getIssuerDN()), e);
                    }
                }
            }
        }

        try {
            buildFileResponse(servletResp, (instanceConfig.getDomainName() + FILE_NAME_SUFFIX_CREDS_ARCHIVE + ArchiveType.ZIP.getFileExtension()),
                ArchiveType.ZIP.getContentType(),
                ArchiveUtils.writeArchive(ArchiveType.ZIP, CollectionUtils.collect(credEntryDescPairs, ZipArchiveEntryPairTransformer.INSTANCE)));
        } catch (ArchiveException e) {
            throw new ToolWebException("Unable to write Discovery credentials archive data.", e);
        }
    }

    @RequestMapping(value = { "/admin/login" }, method = { RequestMethod.GET })
    @RequestView("admin-login")
    public ModelAndView displayAdminLogin() {
        return new ModelAndView();
    }

    @RequestMapping(value = { "/admin" }, method = { RequestMethod.GET })
    @RequestView("admin")
    public ModelAndView displayAdmin() {
        return new ModelAndView();
    }
}
