package gov.hhs.onc.dcdt.mail.utils;

import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultInfo;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolMessageUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.io.IOException;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.context.MessageSource;

public abstract class ToolMailResultStringUtils {
    public final static String DELIM_SECTION = StringUtils.repeat("-", 100);

    public static String messageToString(MimeMessage mimeMessage) throws MailCryptographyException {
        try {
            return IOUtils.toString(mimeMessage.getInputStream());
        } catch (IOException | MessagingException e) {
            throw new MailCryptographyException(String.format("Unable to convert message (class=%s) to string.", ToolClassUtils.getName(MimeMessage.class)), e);
        }
    }

    public static void appendCredentialInfo(DiscoveryTestcaseResultInfo resultInfo, ToolStrBuilder resultStrBuilder, MessageSource msgSource) {
        appendCredentialDescription("dcdt.testcase.discovery.cred.certFound.msg", resultInfo.getCredentialFound(), resultStrBuilder, msgSource);
        resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);

        if (resultInfo.isSuccessful()) {
            resultStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(msgSource, "dcdt.testcase.discovery.cred.foundExpectedCert.msg"), StringUtils.LF);
        } else {
            appendCredentialDescription("dcdt.testcase.discovery.cred.certExpected.msg", resultInfo.getCredentialExpected(), resultStrBuilder, msgSource);
        }
    }

    public static void appendCredentialDescription(String label, DiscoveryTestcaseCredential cred, ToolStrBuilder strBuilder, MessageSource msgSource) {
        strBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(msgSource, label), StringUtils.LF);

        if (cred != null) {
            strBuilder.appendWithDelimiter(cred.toString(), StringUtils.LF);
        } else {
            strBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(msgSource, "dcdt.testcase.discovery.cred.none.msg"), StringUtils.LF);
        }
    }

    public static void appendDecryptionErrorMessage(ToolStrBuilder resultStrBuilder, String errorMsg, MessageSource msgSource) {
        resultStrBuilder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);
        resultStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(msgSource, "dcdt.testcase.discovery.result.decryption.error.msg"), StringUtils.LF);
        resultStrBuilder.appendWithDelimiter(errorMsg, StringUtils.LF);
    }

    public static void appendDecryptedMessage(ToolStrBuilder resultStrBuilder, MimeMessage decryptedMsg, MessageSource msgSource) {
        appendResultInfo(ToolMailResultStringUtils.DELIM_SECTION, resultStrBuilder);
        resultStrBuilder.appendWithDelimiter(ToolMessageUtils.getMessage(msgSource, "dcdt.testcase.discovery.result.decrypted.msg"), StringUtils.LF);

        try {
            ToolMailResultStringUtils.appendResultInfo(ToolMailResultStringUtils.messageToString(decryptedMsg), resultStrBuilder);
        } catch (MailCryptographyException ignored) {
        }
    }

    public static void appendResultInfo(String desc, ToolStrBuilder builder) {
        builder.appendWithDelimiter(StringUtils.SPACE, StringUtils.LF);
        builder.appendWithDelimiter(desc, StringUtils.LF);
    }
}