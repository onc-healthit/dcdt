package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.mail.ToolMailCryptographyException;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMailContentTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.math.BigInteger;
import java.security.cert.CertificateException;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaX509CertificateConverter;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.RecipientInformation;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.springframework.util.InvalidMimeTypeException;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

public abstract class MailCryptographyUtils {
    @SuppressWarnings("unchecked")
    public static Map<BigInteger, KeyTransRecipientInformation> getRecipients(SMIMEEnveloped envelopedMsg) throws MessagingException {
        Collection<RecipientInformation> recipientsStore = (Collection<RecipientInformation>) envelopedMsg.getRecipientInfos().getRecipients();
        Map<BigInteger, KeyTransRecipientInformation> recipientsMap = new LinkedHashMap<>(recipientsStore.size());

        for (RecipientInformation recipient : recipientsStore) {
            if (!KeyTransRecipientInformation.class.isAssignableFrom(recipient.getClass())) {
                throw new ToolMailCryptographyException(String.format("Recipient (class=%s) is not an instance of (class=%s).",
                    ToolClassUtils.getName(recipient), ToolClassUtils.getName(KeyTransRecipientInformation.class)));
            }

            recipientsMap.put(((KeyTransRecipientId) recipient.getRID()).getSerialNumber(), (KeyTransRecipientInformation) recipient);
        }

        return recipientsMap;
    }

    public static void assertIsEnvelopedMessage(ToolMimeMessageHelper mimeMsgHelper) throws MessagingException {
        MimeType contentType = mimeMsgHelper.getContentType();

        if (!ToolMailContentTypeUtils.isEnvelopedData(contentType)) {
            throw new ToolMailCryptographyException(String.format("Invalid Content-Type (%s), expected %s or %s.", contentType,
                MailContentTypes.APP_PKCS7_MIME_ENV, MailContentTypes.APP_X_PKCS7_MIME_ENV));
        }
    }

    public static SMIMEEnveloped getEnvelopedMessage(MimeMessage mimeMsg) throws MessagingException {
        try {
            return new SMIMEEnveloped(mimeMsg);
        } catch (CMSException | MessagingException e) {
            throw new ToolMailCryptographyException(String.format("Unable to get enveloped message from encrypted message (class=%s).",
                ToolClassUtils.getName(MimeMessage.class)), e);
        }
    }

    public static SMIMESigned getSignedMessageEntity(Multipart msgMultiPart) throws MessagingException {
        String contentTypeStr = msgMultiPart.getContentType();

        try {
            MimeType contentType = MimeTypeUtils.parseMimeType(contentTypeStr);

            if (ToolMailContentTypeUtils.isMultipartSigned(contentType)) {
                try {
                    return new SMIMESigned((MimeMultipart) msgMultiPart);
                } catch (CMSException | MessagingException e) {
                    throw new ToolMailCryptographyException(String.format(
                        "Unable to create signed message entity from multipart (class=%s) for content type %s", ToolClassUtils.getName(Multipart.class),
                        contentTypeStr), e);
                }
            } else {
                throw new ToolMailCryptographyException(String.format("Multipart (content type=%s) is not a multipart signature of content type %s or %s",
                    contentTypeStr, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG));
            }
        } catch (InvalidMimeTypeException e) {
            throw new ToolMailCryptographyException(String.format("Unable to parse content type string (%s) into class=%s.", contentTypeStr,
                ToolClassUtils.getName(MimeType.class)), e);
        }
    }

    @SuppressWarnings("unchecked")
    public static CertificateInfo validateSignature(SMIMESigned signedEntity) throws MessagingException {
        Store certStore = signedEntity.getCertificates();

        for (SignerInformation signerInfo : (Collection<SignerInformation>) signedEntity.getSignerInfos().getSigners()) {
            for (X509CertificateHolder certHolder : (Collection<X509CertificateHolder>) certStore.getMatches(signerInfo.getSID())) {
                try {
                    if (signerInfo.verify(new JcaSimpleSignerInfoVerifierBuilder().build(certHolder))) {
                        return new CertificateInfoImpl(new JcaX509CertificateConverter().getCertificate(certHolder));
                    }
                } catch (CMSException | OperatorCreationException | CertificateException ignored) {
                }
            }
        }

        throw new ToolMailCryptographyException("Unable to validate signature.");
    }
}
