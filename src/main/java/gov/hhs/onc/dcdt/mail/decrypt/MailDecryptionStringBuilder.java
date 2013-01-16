package gov.hhs.onc.dcdt.mail.decrypt;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import javax.mail.Address;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
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

public abstract class MailDecryptionStringBuilder
{
	@SuppressWarnings("unchecked")
	public static String envelopedMsgToString(SMIMEEnveloped envelopedMsg)
	{
		StringBuilder builder = new StringBuilder();
		
		try
		{
			Map<BigInteger, KeyTransRecipientInformation> recipientsMap = 
				MailDecryptor.getRecipients(envelopedMsg);
			
			builder.append("recipients=[");
			builder.append(recipientsToString(recipientsMap.values()));
			builder.append("]");
		}
		catch (MailDecryptionException e)
		{
		}
		
		if (builder.length() > 0)
		{
			builder.append(", ");
		}
		
		builder.append("encryptAlg=");
		builder.append(AlgorithmIdentifier.getInstance(envelopedMsg.getEncryptionAlgOID()).getAlgorithm());

		AttributeTable attrTable = envelopedMsg.getUnprotectedAttributes();
		
		if ((attrTable != null) && (attrTable.size() > 0))
		{
			Hashtable<ASN1ObjectIdentifier, ASN1Encodable> attrHashTable = 
				(Hashtable<ASN1ObjectIdentifier, ASN1Encodable>)attrTable.toHashtable();
			List<ASN1ObjectIdentifier> attrKeys = new ArrayList<>(attrHashTable.keySet());
			ASN1ObjectIdentifier attrKey;
			
			builder.append(", attrs=[");
			
			for (int a = 0; a < attrKeys.size(); a++)
			{
				if (a > 0)
				{
					builder.append(", ");
				}
				
				attrKey = attrKeys.get(a);
				
				builder.append(attrKey.getId());
				builder.append("=");
				builder.append(attrHashTable.get(attrKey));
			}
			
			builder.append("]");
		}
		
		return builder.toString();
	}
	
	public static String recipientsToString(KeyTransRecipientInformation ... recipients)
	{
		return recipientsToString(Arrays.asList(recipients));
	}
	
	public static String recipientsToString(Collection<KeyTransRecipientInformation> recipients)
	{
		StringBuilder builder = new StringBuilder();
		KeyTransRecipientId recipientId;
		
		for (KeyTransRecipientInformation recipient : recipients)
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			recipientId = (KeyTransRecipientId)recipient.getRID();
			
			builder.append("{serialNum=");
			builder.append(serialNumToString(recipientId.getSerialNumber()));
			builder.append("}");
		}
		
		return builder.toString();
	}
	
	public static String msgToString(MimeMessage msg)
	{
		StringBuilder builder = new StringBuilder();
		
		try
		{
			builder.append("from=[");
			builder.append(addressesToString(msg.getFrom()));
			builder.append("]");
		}
		catch (MessagingException e)
		{
		}
		
		try
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("to=[");
			builder.append(addressesToString(msg.getRecipients(RecipientType.TO)));
			builder.append("]");
		}
		catch (MessagingException e)
		{
		}
		
		try
		{
			String msgSubject = msg.getSubject();
			
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("subject=");
			builder.append(msgSubject);
		}
		catch (MessagingException e)
		{
		}
		
		// TODO: optionally add message content/body
		
		return builder.toString();
	}
	
	public static String addressesToString(Address[] addresses)
	{
		if (ArrayUtils.isEmpty(addresses))
		{
			return StringUtils.EMPTY;
		}
	
		StringBuilder builder = new StringBuilder();
		
		for (int a = 0; a < addresses.length; a++)
		{
			if (a > 0)
			{
				builder.append(", ");
			}
			
			builder.append(addresses[a]);
		}
		
		return builder.toString();
	}
	
	public static String serialNumToString(BigInteger serialNum)
	{
		return Hex.encodeHexString(serialNum.toByteArray());
	}
}