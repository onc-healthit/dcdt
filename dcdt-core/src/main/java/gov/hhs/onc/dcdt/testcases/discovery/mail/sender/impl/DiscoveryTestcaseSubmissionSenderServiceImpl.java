package gov.hhs.onc.dcdt.testcases.discovery.mail.sender.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.mail.sender.DiscoveryTestcaseSubmissionSenderService;
import java.nio.charset.Charset;
import javax.annotation.Nullable;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseSubmissionSenderServiceImpl extends AbstractToolMailSenderService implements DiscoveryTestcaseSubmissionSenderService {
    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_SUBMISSION = "discoveryTestcaseSubmission";

    public DiscoveryTestcaseSubmissionSenderServiceImpl(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        @Nullable InstanceMailAddressConfig replyToConfig, String mimeMailMsgBeanName) {
        super(mailEnc, velocityEngine, fromConfig, replyToConfig, mimeMailMsgBeanName);
    }

    @Override
    public void send(DiscoveryTestcaseSubmission submission, MailAddress mailAddr) throws Exception {
        this.send(submission, mailAddr, null, null, null);
    }

    @Override
    public void send(DiscoveryTestcaseSubmission submission, MailAddress mailAddr, @Nullable CredentialInfo signingCredInfo,
        @Nullable CertificateInfo encryptingCertInfo, @Nullable MailEncryptionAlgorithm encryptionAlg) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_SUBMISSION, submission);

        this.send(modelMap, modelMap, mailAddr, signingCredInfo, encryptingCertInfo, encryptionAlg);
    }
}
