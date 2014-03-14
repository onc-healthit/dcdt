package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.crypto.mail.MailCryptographyException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.mail.smime.SMIMEEnveloped;

public abstract class ToolMailCryptographyStringUtils {
    public final static String DELIM_ITEM = ", ";

    public static String envelopedMsgToString(SMIMEEnveloped envelopedMsg) {
        ToolStrBuilder builder = new ToolStrBuilder();

        try {
            appendMessageInfo(builder, "recipients", recipientsToString(MailCryptographyUtils.getRecipients(envelopedMsg).values()), true);
        } catch (MailCryptographyException ignored) {
        }

        builder.append("encryptAlg=");
        builder.append(AlgorithmIdentifier.getInstance(envelopedMsg.getEncryptionAlgOID()).getAlgorithm());

        AttributeTable attrTable = envelopedMsg.getUnprotectedAttributes();

        if (attrTable != null && attrTable.size() > 0) {
            appendMessageInfo(builder, "attrs", appendAttributes(builder, attrTable), false);
        }

        return builder.toString();
    }

    @SuppressWarnings("unchecked")
    public static String appendAttributes(ToolStrBuilder builder, AttributeTable attrTable) {
        Hashtable<ASN1ObjectIdentifier, ASN1Encodable> attrHashTable = (Hashtable<ASN1ObjectIdentifier, ASN1Encodable>) attrTable.toHashtable();

        for(Map.Entry<ASN1ObjectIdentifier, ASN1Encodable> entry : attrHashTable.entrySet()) {
            appendDelimiter(builder);
            builder.append(entry.getKey().getId());
            builder.append("=");
            builder.append(entry.getValue());
        }
        return builder.toString();
    }

    public static String recipientsToString(KeyTransRecipientInformation ... recipients) {
        return recipientsToString(Arrays.asList(recipients));
    }

    public static String recipientsToString(Collection<KeyTransRecipientInformation> recipients) {
        ToolStrBuilder builder = new ToolStrBuilder();

        for (KeyTransRecipientInformation recipient : recipients) {
            appendDelimiter(builder);
            builder.append("{serialNum=");
            builder.append(serialNumToString(((KeyTransRecipientId) recipient.getRID()).getSerialNumber()));
            builder.append("}");
        }

        return builder.toString();
    }

    public static String messageHeaderInfoToString(MimeMessage msg) {
        ToolStrBuilder builder = new ToolStrBuilder();

        try {
            appendMessageInfo(builder, "from", addressesToString(msg.getFrom()), true);
            appendMessageInfo(builder, "to", addressesToString(msg.getRecipients(RecipientType.TO)), true);
            builder.append("subject=");
            builder.append(msg.getSubject());
        } catch (MessagingException ignored) {
        }

        return builder.toString();
    }

    public static String addressesToString(Address[] addresses) {
        if (ArrayUtils.isEmpty(addresses)) {
            return StringUtils.EMPTY;
        }

        ToolStrBuilder addrStrBuilder = new ToolStrBuilder();
        addrStrBuilder.appendWithDelimiters(addresses, DELIM_ITEM);

        return addrStrBuilder.build();
    }

    public static String serialNumToString(BigInteger serialNum) {
        return Hex.encodeHexString(serialNum.toByteArray());
    }

    public static void appendMessageInfo(ToolStrBuilder builder, String fieldName, String fieldValue, boolean appendDelim) {
        builder.append(fieldName);
        builder.append("=[");
        builder.append(fieldValue);
        builder.append("]");

        if (appendDelim) {
            appendDelimiter(builder);
        }
    }

    public static void appendDelimiter(ToolStrBuilder builder) {
        if (builder.length() > 0) {
            builder.append(DELIM_ITEM);
        }
    }
}
