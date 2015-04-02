package gov.hhs.onc.dcdt.service.mail.james.utils;

import gov.hhs.onc.dcdt.collections.ToolTransformer;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.mailet.Mail;

public abstract class ToolJamesUtils {
    public static org.apache.mailet.MailAddress transformMailetAddressType(MailAddress addr) throws Exception {
        return new org.apache.mailet.MailAddress(addr.toInternetAddress());
    }

    public static Address transformMailetAddress(org.apache.mailet.MailAddress addr) throws Exception {
        return addr.toInternetAddress();
    }

    @SuppressWarnings({ "unchecked" })
    public static ToolMimeMessageHelper wrapMessage(Mail mail, Charset mailEnc) throws MessagingException {
        MimeMessage msg = new MimeMessage(mail.getMessage());
        msg.setFrom(mail.getSender().toInternetAddress());
        msg.setRecipients(
            RecipientType.TO,
            ToolCollectionUtils.toArray(
                ToolStreamUtils.transform((Collection<org.apache.mailet.MailAddress>) mail.getRecipients(), ToolTransformer.wrap(
                    ToolJamesUtils::transformMailetAddress)), Address.class));
        msg.saveChanges();

        try {
            return new ToolMimeMessageHelper(msg, mailEnc);
        } catch (IOException e) {
            throw new ToolMailException(String.format("Unable to wrap (enc=%s) mail message:\n%s", mailEnc.name(), msg), e);
        }
    }
}
