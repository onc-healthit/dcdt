package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.config.InstanceLdapConfig;
import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.testcases.LocationType;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialLocation;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class ToolMailResultStringUtils {
    public final static String DELIM_SECTION = StringUtils.repeat("-", 100);

    public static String resultToString(EmailInfo emailInfo) {
        ToolStrBuilder resultStrBuilder = new ToolStrBuilder();
        DiscoveryTestcase testcase = emailInfo.getTestcase();
        DiscoveryTestcaseResultInfo resultInfo = emailInfo.getResultInfo();

        if (testcase != null) {
            appendResultInfo("Results for Discovery Testcase ", testcase.getNameDisplay(), resultStrBuilder);
            resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);
        }

        appendResultInfo("Passed Testcase: ", String.valueOf(resultInfo.isSuccessful()), resultStrBuilder);
        resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);

        appendCredentialInfo(resultInfo, resultStrBuilder);

        if (resultInfo.hasDecryptionErrorMessage()) {
            appendResultInfo("Decryption error messages: ", resultStrBuilder);
            resultStrBuilder.appendWithDelimiter(resultInfo.getDecryptionErrorMessage(), StringUtils.LF);
        }

        if (emailInfo.hasDecryptedMessage()) {
            appendResultInfo(DELIM_SECTION, resultStrBuilder);
            appendResultInfo("Decrypted Message: ", resultStrBuilder);

            try {
                appendResultInfo(messageToString(emailInfo.getDecryptedMessage()), resultStrBuilder);
            } catch (MailCryptographyException ignored) {
            }
        }

        return resultStrBuilder.build();
    }

    public static String messageToString(MimeMessage mimeMessage) throws MailCryptographyException {
        try {
            return IOUtils.toString(mimeMessage.getInputStream());
        } catch (IOException | MessagingException e) {
            throw new MailCryptographyException(String.format("Unable to convert message (class=%s) to string.", ToolClassUtils.getName(MimeMessage.class)), e);
        }
    }

    @SuppressWarnings({ "ConstantConditions" })
    public static String credentialToString(DiscoveryTestcaseCredential cred, String label) {
        ToolStrBuilder credStrBuilder = new ToolStrBuilder();
        credStrBuilder.appendWithDelimiter(label, StringUtils.LF);

        if (cred != null) {
            credStrBuilder.append(cred.getName());
            appendResultInfo("- Valid: ", String.valueOf(cred.isValid()), credStrBuilder);
            appendResultInfo("- Binding Type: ", cred.getBindingType().toString(), credStrBuilder);

            DiscoveryTestcaseCredentialLocation location = cred.getLocation();
            credStrBuilder.appendWithDelimiter("- Location: ", StringUtils.LF);
            LocationType locType = location.getType();
            appendResultInfo("\tType: ", locType.toString(), credStrBuilder);
            appendResultInfo("\tMail Address: ", location.getMailAddress().toAddress(), credStrBuilder);

            if (location.hasLdapConfig()) {
                InstanceLdapConfig ldapConfig = location.getLdapConfig();
                appendResultInfo("\tBind Address: ", ldapConfig.getBindAddress().toString(), credStrBuilder);
                appendResultInfo("\tBind Port: ", String.valueOf(ldapConfig.getBindPort()), credStrBuilder);
            }

            appendResultInfo("- Description: ", cred.getDescription().getText(), credStrBuilder);
        } else {
            credStrBuilder.append("None");
        }

        return credStrBuilder.build();
    }

    public static void appendCredentialInfo(DiscoveryTestcaseResultInfo resultInfo, ToolStrBuilder resultStrBuilder) {
        resultStrBuilder.appendWithDelimiter(credentialToString(resultInfo.getCredentialFound(), "Certificate Found: "), StringUtils.LF);
        resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);

        if (resultInfo.isSuccessful()) {
            resultStrBuilder.appendWithDelimiter("Certificate expected is the same as certificate found.", StringUtils.LF);
        } else {
            resultStrBuilder.appendWithDelimiter(credentialToString(resultInfo.getCredentialExpected(), "Certificate Expected: "), StringUtils.LF);
        }
    }

    public static void appendResultInfo(String desc, ToolStringUtils.ToolStrBuilder builder) {
        builder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);
        builder.appendWithDelimiter(desc, StringUtils.LF);
    }

    public static void appendResultInfo(String label, String desc, ToolStrBuilder builder) {
        builder.appendWithDelimiter(label, StringUtils.LF);
        builder.append(desc);
    }
}