package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.mail.impl.EmailInfoImpl;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.mail.utils.ToolMailResultStringUtils;
import gov.hhs.onc.dcdt.crypto.mail.decrypt.MailDecryptor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultGenerator;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseResultGeneratorImpl")
public class DiscoveryTestcaseResultGeneratorImpl extends AbstractToolTestcaseResultGenerator<DiscoveryTestcaseResultConfig, DiscoveryTestcaseResultInfo>
    implements DiscoveryTestcaseResultGenerator {

    @Resource(name = "certificateValidators")
    private Set<CertificateValidator> certificateValidators;

    @Override
    public EmailInfo generateTestcaseResult(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        EmailInfo emailInfo = runDiscoveryTestcase(emailInStream, getTestcasesMap(discoveryTestcases));
        DiscoveryTestcaseResultInfo resultInfo = emailInfo.getResultInfo();

        if (emailInfo.hasTestcase() && resultInfo.getCredentialFound() == resultInfo.getCredentialExpected()) {
            resultInfo.setSuccessful(true);
        }

        emailInfo.setMessage(ToolMailResultStringUtils.resultToString(emailInfo));

        return emailInfo;
    }

    @Override
    public EmailInfo runDiscoveryTestcase(InputStream emailInStream, Map<String, DiscoveryTestcase> discoveryTestcaseMap) {
        EmailInfo emailInfo = new EmailInfoImpl();
        DiscoveryTestcaseResultInfo resultInfo = new DiscoveryTestcaseResultInfoImpl();
        emailInfo.setResultInfo(resultInfo);

        ToolStrBuilder decryptionErrorBuilder = new ToolStrBuilder();

        try {
            MimeMessage origMimeMessage = MailCryptographyUtils.getMimeMessage(emailInStream);
            emailInfo.setEncryptedMessage(origMimeMessage);
            MailCryptographyUtils.parseMessageHeaders(origMimeMessage, emailInfo);
            String testcaseAddr = emailInfo.getToAddress();

            if (discoveryTestcaseMap.containsKey(testcaseAddr)) {
                DiscoveryTestcase discoveryTestcase = discoveryTestcaseMap.get(testcaseAddr);
                emailInfo.setTestcase(discoveryTestcase);
                resultInfo.setCredentialExpected(getCredentialExpected(discoveryTestcase));

                // noinspection ConstantConditions
                for (DiscoveryTestcaseCredential cred : discoveryTestcase.getCredentials()) {
                    if (MailDecryptor.decryptMail(emailInfo, decryptionErrorBuilder, origMimeMessage, cred, this.certificateValidators)) {
                        break;
                    }
                }
            } else {
                decryptionErrorBuilder
                    .appendWithDelimiter(String.format("Unable to find a matching testcase mail address=(%s).", testcaseAddr), StringUtils.LF);
            }
        } catch (MailCryptographyException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }

        resultInfo.setDecryptionErrorMessage(decryptionErrorBuilder.build());

        return emailInfo;
    }

    @SuppressWarnings({ "ConstantConditions" })
    private Map<String, DiscoveryTestcase> getTestcasesMap(List<DiscoveryTestcase> discoveryTestcases) {
        Map<String, DiscoveryTestcase> discoveryTestcaseMap = new HashMap<>(discoveryTestcases.size());

        for (DiscoveryTestcase testcase : discoveryTestcases) {
            discoveryTestcaseMap.put(testcase.getMailAddress().toAddress(), testcase);
        }

        return discoveryTestcaseMap;
    }

    private DiscoveryTestcaseCredential getCredentialExpected(DiscoveryTestcase discoveryTestcase) {
        if (discoveryTestcase.hasTargetCredentials()) {
            for (DiscoveryTestcaseCredential targetCred : discoveryTestcase.getTargetCredentials()) {
                if (targetCred.isValid()) {
                    return targetCred;
                }
            }
        }

        return null;
    }
}
