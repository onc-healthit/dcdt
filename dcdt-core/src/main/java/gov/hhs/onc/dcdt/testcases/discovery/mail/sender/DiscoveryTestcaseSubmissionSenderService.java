package gov.hhs.onc.dcdt.testcases.discovery.mail.sender;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.sender.ToolMailSenderService;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import javax.annotation.Nullable;

public interface DiscoveryTestcaseSubmissionSenderService extends ToolMailSenderService {
    public void send(DiscoveryTestcaseSubmission submission, MailAddress mailAddr) throws Exception;

    public void send(DiscoveryTestcaseSubmission submission, MailAddress mailAddr, @Nullable CredentialInfo signingCredInfo,
        @Nullable CertificateInfo encryptingCertInfo, @Nullable MailEncryptionAlgorithm encryptionAlg) throws Exception;
}
