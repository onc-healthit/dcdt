package gov.hhs.onc.dcdt.testcases.discovery.results.sender.impl;

import gov.hhs.onc.dcdt.config.instance.InstanceMailAddressConfig;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MimeAttachmentResource;
import gov.hhs.onc.dcdt.mail.sender.impl.AbstractToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultCredentialType;
import gov.hhs.onc.dcdt.testcases.discovery.results.sender.DiscoveryTestcaseResultSenderService;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.velocity.app.VelocityEngine;
import org.springframework.ui.ModelMap;

public class DiscoveryTestcaseResultSenderServiceImpl extends AbstractToolMailSenderService implements DiscoveryTestcaseResultSenderService {
    private final static String MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT = "discoveryTestcaseResult";

    public DiscoveryTestcaseResultSenderServiceImpl(Charset mailEnc, VelocityEngine velocityEngine, InstanceMailAddressConfig fromConfig,
        @Nullable InstanceMailAddressConfig replyToConfig, String mimeMailMsgBeanName) {
        super(mailEnc, velocityEngine, fromConfig, replyToConfig, mimeMailMsgBeanName);
    }

    @Override
    public void send(DiscoveryTestcaseResult discoveryTestcaseResult, MailAddress to) throws Exception {
        ModelMap modelMap = new ModelMap();
        modelMap.addAttribute(MODEL_ATTR_NAME_TESTCASE_DISCOVERY_RESULT, discoveryTestcaseResult);

        // noinspection ConstantConditions
        List<MimeAttachmentResource> attachments = new ArrayList<>(DiscoveryTestcaseResultCredentialType.values().length);
        MimeAttachmentResource attachment;

        for (DiscoveryTestcaseResultCredentialType discoveryTestcaseResultCredType : EnumSet.allOf(DiscoveryTestcaseResultCredentialType.class)) {
            if ((attachment = buildDiscoveryTestcaseResultCredentialAttachment(discoveryTestcaseResult, discoveryTestcaseResultCredType)) != null) {
                attachments.add(attachment);
            }
        }

        this.send(modelMap, modelMap, to, attachments);
    }

    @Nullable
    private static MimeAttachmentResource buildDiscoveryTestcaseResultCredentialAttachment(DiscoveryTestcaseResult discoveryTestcaseResult,
        DiscoveryTestcaseResultCredentialType discoveryTestcaseResultCredType) throws Exception {
        DiscoveryTestcaseCredential discoveryTestcaseResultCred;
        CredentialInfo discoveryTestcaseResultCredInfo;
        CertificateInfo discoveryTestcaseResultCredCertInfo;

        // noinspection ConstantConditions
        return ((discoveryTestcaseResult.hasCredential(discoveryTestcaseResultCredType)
            && (discoveryTestcaseResultCred = discoveryTestcaseResult.getCredential(discoveryTestcaseResultCredType)).hasCredentialInfo()
            && (discoveryTestcaseResultCredInfo = discoveryTestcaseResultCred.getCredentialInfo()).hasCertificateDescriptor() && (discoveryTestcaseResultCredCertInfo =
            discoveryTestcaseResultCredInfo.getCertificateDescriptor()).hasCertificate()) ? new MimeAttachmentResource(CertificateUtils.writeCertificate(
            discoveryTestcaseResultCredCertInfo.getCertificate(), DataEncoding.DER), discoveryTestcaseResultCred.getName(), discoveryTestcaseResultCredCertInfo
            .getCertificateType().getContentType(), discoveryTestcaseResult.getTestcase().getName()
            + discoveryTestcaseResultCredType.getAttachmentFileNameSuffix() + DataEncoding.DER.getFileExtension()) : null);
    }
}
