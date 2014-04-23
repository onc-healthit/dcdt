package gov.hhs.onc.dcdt.web.controller.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential.DiscoveryTestcaseCredentialTypePredicate;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.io.IOException;
import java.nio.charset.Charset;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.annotation.Resource;
import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("discoveryController")
@DisplayController
public class DiscoveryController extends AbstractToolController {
    private final static String HEADER_NAME_CONTENT_DISPOSITION = "Content-Disposition";

    private final static String HEADER_VALUE_PREFIX_CONTENT_DISPOSITION_ANCHOR = "attachment; filename=";

    @Resource(name = "charsetUtf8")
    private Charset anchorEnc;

    @RequestMapping(value = { "/discovery/anchor" }, method = { RequestMethod.GET })
    public void downloadAnchor(HttpServletResponse servletResp) throws ToolWebException {
        DiscoveryTestcaseCredential anchorCred =
            CollectionUtils.find(ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcaseCredential.class),
                DiscoveryTestcaseCredentialTypePredicate.INSTANCE_CA);
        CredentialInfo anchorCredInfo;
        CertificateInfo anchorCertInfo;

        // noinspection ConstantConditions
        if ((anchorCred == null) || !anchorCred.hasName() || !anchorCred.hasCredentialConfig() || !anchorCred.hasCredentialInfo()
            || !(anchorCredInfo = anchorCred.getCredentialInfo()).hasCertificateDescriptor()
            || !(anchorCertInfo = anchorCredInfo.getCertificateDescriptor()).hasCertificateType()) {
            throw new ToolWebException("Discovery trust anchor certificate is unavailable - instance configuration must be set.");
        }

        X509Certificate anchorCert = anchorCertInfo.getCertificate();
        byte[] anchorCertData;
        ServletOutputStream servletOutStream;

        try {
            anchorCertData = CertificateUtils.writeCertificate(anchorCert, DataEncoding.DER);

            servletResp.setHeader(HEADER_NAME_CONTENT_DISPOSITION,
                (HEADER_VALUE_PREFIX_CONTENT_DISPOSITION_ANCHOR + ToolStringUtils.quote(anchorCred.getName() + DataEncoding.DER.getFileExtension())));
            servletResp.setContentLength(anchorCertData.length);
            // noinspection ConstantConditions
            servletResp.setContentType(anchorCertInfo.getCertificateType().getContentType().toString());

            IOUtils.write(anchorCertData, (servletOutStream = servletResp.getOutputStream()));
            servletOutStream.flush();
        } catch (CryptographyException | IOException e) {
            // noinspection ConstantConditions
            throw new ToolWebException(String.format(
                "Unable to write Discovery trust anchor certificate (subj={%s}, serialNum=%s, issuer={%s}) data to HTTP servlet output stream.", anchorCert
                    .getSubjectX500Principal().getName(), anchorCertInfo.getSerialNumber(), anchorCert.getIssuerX500Principal().getName()), e);
        }
    }

    @RequestMapping(value = { "/discovery" }, method = { RequestMethod.GET })
    @RequestView("discovery")
    public ModelAndView displayDiscovery() {
        return new ModelAndView();
    }

    @ModelAttribute("discoveryTestcases")
    private List<DiscoveryTestcase> getDiscoveryTestcasesModelAttribute() {
        return ToolBeanFactoryUtils.getBeansOfType(this.appContext, DiscoveryTestcase.class);
    }
}
