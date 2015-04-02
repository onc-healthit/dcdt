package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.compress.ArchiveType;
import gov.hhs.onc.dcdt.compress.utils.ArchiveUtils;
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
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Map.Entry;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.IteratorUtils;
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

        EnumSet<DataEncoding> dataEncs = EnumSet.allOf(DataEncoding.class);
        List<DiscoveryTestcaseCredential> creds =
            IteratorUtils.toList(ToolIteratorUtils.chainedIterator(ToolStreamUtils.transform(
                ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class), DiscoveryTestcase::extractCredentials)));
        creds.add(ToolStreamUtils.find(ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcaseCredential.class),
            DiscoveryTestcaseCredential::isInstanceCa));

        List<Entry<String, byte[]>> credEntryDescPairs = new ArrayList<>(creds.size() * 3);
        String credName;
        CredentialInfo credInfo;
        KeyInfo keyInfo;
        CertificateInfo certInfo;
        X509Certificate cert;

        for (DiscoveryTestcaseCredential cred : creds) {
            if (!cred.hasCredentialInfo()) {
                continue;
            }

            credName = cred.getName();

            // noinspection ConstantConditions
            if ((credInfo = cred.getCredentialInfo()).hasKeyDescriptor() && (keyInfo = credInfo.getKeyDescriptor()).hasPrivateKey()) {
                for (DataEncoding dataEnc : dataEncs) {
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

                for (DataEncoding dataEnc : dataEncs) {
                    try {
                        credEntryDescPairs.add(new ImmutablePair<>((credName + dataEnc.getFileExtension()), CertificateUtils.writeCertificate(
                            certInfo.getCertificate(), dataEnc)));
                    } catch (CryptographyException e) {
                        // noinspection ConstantConditions
                        throw new ToolWebException(String.format(
                            "Unable to write Discovery credential (name=%s) certificate (subj={%s}, serialNum=%s, issuer={%s}) data.", credName, cert
                                .getSubjectX500Principal().getName(), certInfo.getSerialNumber(), cert.getIssuerX500Principal().getName()), e);
                    }
                }
            }
        }

        try {
            buildFileResponse(servletResp, (instanceConfig.getDomainName() + FILE_NAME_SUFFIX_CREDS_ARCHIVE + ArchiveType.ZIP.getFileExtension()),
                ArchiveType.ZIP.getContentType(),
                ArchiveUtils.writeArchive(ArchiveType.ZIP, ToolStreamUtils.transform(credEntryDescPairs, ArchiveUtils::transformZipArchiveEntryPair)));
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
