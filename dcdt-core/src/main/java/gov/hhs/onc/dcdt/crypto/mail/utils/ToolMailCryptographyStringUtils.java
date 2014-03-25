package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Map;
import javax.mail.MessagingException;
import org.apache.commons.codec.binary.Hex;
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
        } catch (MessagingException ignored) {
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

        for (Map.Entry<ASN1ObjectIdentifier, ASN1Encodable> entry : attrHashTable.entrySet()) {
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
