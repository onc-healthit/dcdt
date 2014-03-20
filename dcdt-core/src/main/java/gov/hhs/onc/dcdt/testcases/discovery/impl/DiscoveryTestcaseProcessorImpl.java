package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailDecryptionUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseDescription;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseSubmission;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseConfig;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseProcessor;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.MessagingException;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseProcessorImpl")
public class DiscoveryTestcaseProcessorImpl
    extends
    AbstractToolTestcaseProcessor<DiscoveryTestcaseDescription, DiscoveryTestcaseConfig, DiscoveryTestcaseResult, DiscoveryTestcase, DiscoveryTestcaseSubmission>
    implements DiscoveryTestcaseProcessor {
    @Autowired
    private Set<CertificateValidator> certificateValidators;

    @Autowired
    private List<DiscoveryTestcase> discoveryTestcases;

    @Override
    public DiscoveryTestcaseResult processDiscoveryTestcase(InputStream emailInStream) {
        return processDiscoveryTestcase(emailInStream, this.discoveryTestcases);
    }

    @Override
    public DiscoveryTestcaseResult processDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        DiscoveryTestcaseResult result = runDecryptionSteps(emailInStream, discoveryTestcases);
        result.setSuccessful(result.hasTestcase() && result.getCredentialFound() == result.getCredentialExpected());

        return result;
    }

    @Override
    public DiscoveryTestcaseResult runDecryptionSteps(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        MailInfo mailInfo = ToolBeanFactoryUtils.createBeanOfType(this.appContext, MailInfo.class);
        DiscoveryTestcaseResult result = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DiscoveryTestcaseResult.class);
        // noinspection ConstantConditions
        result.setMailInfo(mailInfo);

        Map<MailAddress, DiscoveryTestcase> discoveryTestcaseMap =
            ToolMapUtils.putAll(new LinkedHashMap<MailAddress, DiscoveryTestcase>(),
                CollectionUtils.collect(discoveryTestcases, new DiscoveryTestcaseMailAddressTestcasePairTransformer()));

        ToolStrBuilder decryptionErrorBuilder = new ToolStrBuilder();

        try {
            ToolMimeMessageHelper mimeMessageHelper = new ToolMimeMessageHelper(MailCryptographyUtils.getMimeMessage(emailInStream));
            // noinspection ConstantConditions
            mailInfo.setEncryptedMessageHelper(mimeMessageHelper);
            MailAddress testcaseAddr = mimeMessageHelper.getTo();

            // noinspection ConstantConditions
            if (discoveryTestcaseMap.containsKey(testcaseAddr)) {
                DiscoveryTestcase discoveryTestcase = discoveryTestcaseMap.get(testcaseAddr);
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
                decryptionErrorBuilder
                    .appendWithDelimiter(String.format("Unable to find a matching testcase mail address=(%s).", testcaseAddr), StringUtils.LF);
            }
        } catch (MailCryptographyException | MessagingException | IOException e) {
            decryptionErrorBuilder.appendWithDelimiter(e.getMessage(), StringUtils.LF);
        }

        // noinspection ConstantConditions
        mailInfo.setDecryptionErrorMessage(decryptionErrorBuilder.build());

        return result;
    }

    private static class DiscoveryTestcaseMailAddressTestcasePairTransformer extends
        AbstractToolTransformer<DiscoveryTestcase, Pair<MailAddress, DiscoveryTestcase>> {
        @Override
        protected Pair<MailAddress, DiscoveryTestcase> transformInternal(DiscoveryTestcase discoveryTestcase) throws Exception {
            return new ImmutablePair<>(discoveryTestcase.getMailAddress(), discoveryTestcase);
        }
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
