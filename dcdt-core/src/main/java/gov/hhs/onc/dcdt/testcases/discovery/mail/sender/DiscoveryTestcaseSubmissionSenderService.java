package gov.hhs.onc.dcdt.testcases.discovery.mail.sender;

import gov.hhs.onc.dcdt.crypto.EncryptionAlgorithm;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.sender.TemplateMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseSubmissionSenderService extends TemplateMailSenderService {
    public void send(DiscoveryTestcaseSubmission submission, MailAddress toAddr) throws Exception;

    public void send(DiscoveryTestcaseSubmission submission, MailAddress toAddr, @Nullable CredentialInfo signerCredInfo,
        @Nullable CertificateInfo encryptionCertInfo, @Nullable EncryptionAlgorithm encryptionAlg) throws Exception;
}
