package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.config.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseResultSenderServiceImpl extends AbstractToolMailSenderService implements DiscoveryTestcaseResultSenderService {
    private final static String ATTACHMENT_FILE_NAME_DELIM = "_";

    private final static String ATTACHMENT_FILE_NAME_SUFFIX_TESTCASE_DISCOVERY_CRED_EXPECTED = ATTACHMENT_FILE_NAME_DELIM + "expected"
        + CryptographyUtils.FILE_EXT_DER;
    private final static String ATTACHMENT_FILE_NAME_SUFFIX_TESTCASE_DISCOVERY_CRED_FOUND = ATTACHMENT_FILE_NAME_DELIM + "found"
        + CryptographyUtils.FILE_EXT_DER;

    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY = "discoveryTestcase";
    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT = MODEL_ATTR_NAME_TESTCASE_DISCOVERY + "Result";

    public DiscoveryTestcaseResultSenderServiceImpl(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        String mimeMailMsgBeanName) {
        super(mailEnc, velocityEngine, fromConfig, mimeMailMsgBeanName);
    }

    @Override
    public void send(DiscoveryTestcase discoveryTestcase, DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress to) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY, discoveryTestcase);
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        List<MimeAttachmentResource> attachments = null;

        if (discoveryTestcaseResult != null) {
            attachments = new ArrayList<>(2);

            String discoveryTestcaseName = discoveryTestcase.getName();
            MimeAttachmentResource attachment;

            if (discoveryTestcaseResult.hasCredentialExpected()
                && ((attachment =
                    buildDiscoveryTestcaseCredentialAttachment(discoveryTestcaseResult.getCredentialExpected(),
                        (discoveryTestcaseName + ATTACHMENT_FILE_NAME_SUFFIX_TESTCASE_DISCOVERY_CRED_EXPECTED))) != null)) {
                attachments.add(attachment);
            }

            if (discoveryTestcaseResult.hasCredentialFound()
                && ((attachment =
                    buildDiscoveryTestcaseCredentialAttachment(discoveryTestcaseResult.getCredentialFound(),
                        (discoveryTestcaseName + ATTACHMENT_FILE_NAME_SUFFIX_TESTCASE_DISCOVERY_CRED_FOUND))) != null)) {
                attachments.add(attachment);
            }
        }

        this.send(modelMap, modelMap, to, attachments);
    }

    @Nullable
    private static MimeAttachmentResource buildDiscoveryTestcaseCredentialAttachment(DiscoveryTestcaseCredential discoveryTestcaseCred, String filename)
        throws Exception {
        CredentialInfo discoveryTestcaseCredInfo;
        CertificateInfo discoveryTestcaseCredCertInfo;

        // noinspection ConstantConditions
        return ((discoveryTestcaseCred.hasCredentialInfo()
            && (discoveryTestcaseCredInfo = discoveryTestcaseCred.getCredentialInfo()).hasCertificateDescriptor() && (discoveryTestcaseCredCertInfo =
            discoveryTestcaseCredInfo.getCertificateDescriptor()).hasCertificate()) ? new MimeAttachmentResource(CertificateUtils.writeCertificate(
            discoveryTestcaseCredCertInfo.getCertificate(), DataEncoding.DER), discoveryTestcaseCred.getName(), discoveryTestcaseCredCertInfo
            .getCertificateType().getContentType(), filename) : null);
    }
}
