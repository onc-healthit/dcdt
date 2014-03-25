package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailDecryptionUtils;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.util.Set;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseProcessorImpl")
public class DiscoveryTestcaseProcessorImpl
    extends
    AbstractToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcaseResult, DiscoveryTestcase, DiscoveryTestcaseSubmission>
    implements DiscoveryTestcaseProcessor {
    @Autowired
    private Set<CertificateValidator> certificateValidators;

    @Override
    public DiscoveryTestcaseResult process(ToolMimeMessageHelper mimeMsgHelper, @Nullable DiscoveryTestcase discoveryTestcase) {
        DiscoveryTestcaseResult result = runDecryptionSteps(mimeMsgHelper, discoveryTestcase);
        result.setSuccessful(result.hasTestcase() && (result.getCredentialFound() == result.getCredentialExpected()));

        return result;
    }

    private DiscoveryTestcaseResult runDecryptionSteps(ToolMimeMessageHelper mimeMsgHelper, @Nullable DiscoveryTestcase discoveryTestcase) {
        MailInfo mailInfo = ToolBeanFactoryUtils.createBeanOfType(this.appContext, MailInfo.class);
        DiscoveryTestcaseResult result = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DiscoveryTestcaseResult.class);
        // noinspection ConstantConditions
        result.setMailInfo(mailInfo);

        ToolStrBuilder decryptionErrorBuilder = new ToolStrBuilder();

        try {
            MailCryptographyUtils.assertIsEnvelopedMessage(mimeMsgHelper);
            // noinspection ConstantConditions
            mailInfo.setEncryptedMessageHelper(mimeMsgHelper);

            if (discoveryTestcase != null) {
                result.setTestcase(discoveryTestcase);
                result.setCredentialExpected(getCredentialExpected(discoveryTestcase));

                // noinspection ConstantConditions
                for (DiscoveryTestcaseCredential cred : discoveryTestcase.getCredentials()) {
                    if (MailDecryptionUtils.decryptMail(mailInfo, decryptionErrorBuilder, cred, this.certificateValidators)) {
                        result.setCredentialFound(cred);
                        break;
                    }
                }
            } else {
                decryptionErrorBuilder.appendWithDelimiter(
                    String.format("Unable to find a matching Discovery testcase for mail address: %s", mimeMsgHelper.getTo()), StringUtils.LF);
            }
        } catch (MessagingException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }

        // noinspection ConstantConditions
        mailInfo.setDecryptionErrorMessage(decryptionErrorBuilder.build());

        return result;
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
