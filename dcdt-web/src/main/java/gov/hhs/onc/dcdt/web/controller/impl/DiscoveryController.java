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
import gov.hhs.onc.dcdt.web.ToolWebException;
import gov.hhs.onc.dcdt.web.controller.DisplayController;
import gov.hhs.onc.dcdt.web.view.RequestView;
import java.security.cert.X509Certificate;
import java.util.List;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.servlet.ModelAndView;

@Controller("discoveryController")
@DisplayController
public class DiscoveryController extends AbstractToolController {
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

        try {
            // noinspection ConstantConditions
            buildFileResponse(servletResp, (anchorCred.getName() + DataEncoding.DER.getFileExtension()), anchorCertInfo.getCertificateType().getContentType(),
                CertificateUtils.writeCertificate(anchorCert, DataEncoding.DER));
        } catch (CryptographyException e) {
            // noinspection ConstantConditions
            throw new ToolWebException(String.format("Unable to write Discovery trust anchor certificate (subj={%s}, serialNum=%s, issuer={%s}) data.",
                anchorCert.getSubjectX500Principal().getName(), anchorCertInfo.getSerialNumber(), anchorCert.getIssuerX500Principal().getName()), e);
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
