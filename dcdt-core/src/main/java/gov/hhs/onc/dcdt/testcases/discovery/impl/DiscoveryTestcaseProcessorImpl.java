package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.crypto.mail.decrypt.MailDecryptor;
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
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import javax.mail.internet.MimeMessage;
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
    public MailInfo processDiscoveryTestcase(InputStream emailInStream) {
        return processDiscoveryTestcase(emailInStream, this.discoveryTestcases);
    }

    @Override
    public MailInfo processDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        MailInfo mailInfo = runDecryptionSteps(emailInStream, discoveryTestcases);
        DiscoveryTestcaseResult result = mailInfo.getResult();
        // noinspection ConstantConditions
        result.setSuccessful(mailInfo.hasTestcase() && result.getCredentialFound() == result.getCredentialExpected());
        mailInfo.setMessage(mailInfo.toString());

        return mailInfo;
    }

    @Override
    public MailInfo runDecryptionSteps(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        MailInfo mailInfo = ToolBeanFactoryUtils.createBeanOfType(this.appContext, MailInfo.class);
        DiscoveryTestcaseResult result = ToolBeanFactoryUtils.createBeanOfType(this.appContext, DiscoveryTestcaseResult.class);
        // noinspection ConstantConditions
        mailInfo.setResult(result);

        Map<String, DiscoveryTestcase> discoveryTestcaseMap =
            ToolMapUtils.putAll(new LinkedHashMap<String, DiscoveryTestcase>(),
                CollectionUtils.collect(discoveryTestcases, new DiscoveryTestcaseMailAddressTestcasePairTransformer()));

        ToolStrBuilder decryptionErrorBuilder = new ToolStrBuilder();

        try {
            MimeMessage origMimeMessage = MailCryptographyUtils.getMimeMessage(emailInStream);
            mailInfo.setEncryptedMessage(origMimeMessage);
            MailCryptographyUtils.parseMessageHeaders(origMimeMessage, mailInfo);
            String testcaseAddr = mailInfo.getToAddress();

            // noinspection ConstantConditions
            if (discoveryTestcaseMap.containsKey(testcaseAddr)) {
                DiscoveryTestcase discoveryTestcase = discoveryTestcaseMap.get(testcaseAddr);
                mailInfo.setTestcase(discoveryTestcase);
                // noinspection ConstantConditions
                result.setCredentialExpected(getCredentialExpected(discoveryTestcase));

                // noinspection ConstantConditions
                for (DiscoveryTestcaseCredential cred : discoveryTestcase.getCredentials()) {
                    if (MailDecryptor.decryptMail(mailInfo, decryptionErrorBuilder, origMimeMessage, cred, this.certificateValidators)) {
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

        // noinspection ConstantConditions
        result.setDecryptionErrorMessage(decryptionErrorBuilder.build());

        return mailInfo;
    }

    private static class DiscoveryTestcaseMailAddressTestcasePairTransformer extends
        AbstractToolTransformer<DiscoveryTestcase, Pair<String, DiscoveryTestcase>> {
        @Override
        protected Pair<String, DiscoveryTestcase> transformInternal(DiscoveryTestcase discoveryTestcase) throws Exception {
            // noinspection ConstantConditions
            return new ImmutablePair<>(discoveryTestcase.getMailAddress().toAddress(), discoveryTestcase);
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
