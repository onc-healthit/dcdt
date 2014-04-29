package gov.hhs.onc.dcdt.service.mail.james.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.ToolMailException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Collection;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.mailet.Mail;

public abstract class ToolJamesUtils {
    public static class MailetAddressTypeTransformer extends AbstractToolTransformer<MailAddress, org.apache.mailet.MailAddress> {
        public final static MailetAddressTypeTransformer INSTANCE = new MailetAddressTypeTransformer();

        @Override
        protected org.apache.mailet.MailAddress transformInternal(MailAddress addr) throws Exception {
            return new org.apache.mailet.MailAddress(addr.toInternetAddress());
        }
    }

    public static class MailetAddressTransformer extends AbstractToolTransformer<org.apache.mailet.MailAddress, Address> {
        public final static MailetAddressTransformer INSTANCE = new MailetAddressTransformer();

        @Override
        protected Address transformInternal(org.apache.mailet.MailAddress addr) throws Exception {
            return addr.toInternetAddress();
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static ToolMimeMessageHelper wrapMessage(Mail mail, Charset mailEnc) throws MessagingException {
        MimeMessage msg = new MimeMessage(mail.getMessage());
        msg.setFrom(mail.getSender().toInternetAddress());
        msg.setRecipients(
            RecipientType.TO,
            ToolCollectionUtils.toArray(
                CollectionUtils.collect(((Collection<org.apache.mailet.MailAddress>) mail.getRecipients()), MailetAddressTransformer.INSTANCE), Address.class));
        msg.saveChanges();

        try {
            return new ToolMimeMessageHelper(msg, mailEnc);
        } catch (IOException e) {
            throw new ToolMailException(String.format("Unable to wrap (enc=%s) mail message:\n%s", mailEnc.name(), msg), e);
        }
    }
}
