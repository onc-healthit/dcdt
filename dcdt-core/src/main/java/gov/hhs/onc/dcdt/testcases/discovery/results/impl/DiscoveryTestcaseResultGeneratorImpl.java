package gov.hhs.onc.dcdt.testcases.discovery.results.impl;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.crypto.certs.CertificateValidator;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.crypto.mail.utils.MailCryptographyUtils;
import gov.hhs.onc.dcdt.crypto.mail.decrypt.MailDecryptor;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultConfig;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultGenerator;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultGenerator;
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
import org.springframework.beans.BeansException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseResultGeneratorImpl")
public class DiscoveryTestcaseResultGeneratorImpl extends AbstractToolTestcaseResultGenerator<DiscoveryTestcaseResultConfig, DiscoveryTestcaseResultInfo>
    implements DiscoveryTestcaseResultGenerator, ApplicationContextAware {

    @Autowired
    private Set<CertificateValidator> certificateValidators;

    private AbstractApplicationContext appContext;

    @Override
    public EmailInfo generateTestcaseResult(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        EmailInfo emailInfo = runDiscoveryTestcase(emailInStream, discoveryTestcases);
        DiscoveryTestcaseResultInfo resultInfo = emailInfo.getResultInfo();
        resultInfo.setSuccessful(emailInfo.hasTestcase() && resultInfo.getCredentialFound() == resultInfo.getCredentialExpected());
        emailInfo.setMessage(emailInfo.toString());

        return emailInfo;
    }

    @Override
    public EmailInfo runDiscoveryTestcase(InputStream emailInStream, List<DiscoveryTestcase> discoveryTestcases) {
        EmailInfo emailInfo = ToolBeanFactoryUtils.createBeanOfType(this.appContext, EmailInfo.class);
        DiscoveryTestcaseResultInfo resultInfo = new DiscoveryTestcaseResultInfoImpl();
        // noinspection ConstantConditions
        emailInfo.setResultInfo(resultInfo);

        Map<String, DiscoveryTestcase> discoveryTestcaseMap =
            ToolMapUtils.putAll(new LinkedHashMap<String, DiscoveryTestcase>(),
                CollectionUtils.collect(discoveryTestcases, new DiscoveryTestcaseMailAddressTestcasePairTransformer()));

        ToolStrBuilder decryptionErrorBuilder = new ToolStrBuilder();

        try {
            MimeMessage origMimeMessage = MailCryptographyUtils.getMimeMessage(emailInStream);
            emailInfo.setEncryptedMessage(origMimeMessage);
            MailCryptographyUtils.parseMessageHeaders(origMimeMessage, emailInfo);
            String testcaseAddr = emailInfo.getToAddress();

            // noinspection ConstantConditions
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

    private class DiscoveryTestcaseMailAddressTestcasePairTransformer extends AbstractToolTransformer<DiscoveryTestcase, Pair<String, DiscoveryTestcase>> {
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

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) applicationContext;
    }
}
