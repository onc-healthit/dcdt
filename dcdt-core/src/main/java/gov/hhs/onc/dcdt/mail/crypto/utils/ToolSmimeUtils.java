package gov.hhs.onc.dcdt.mail.crypto.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateSerialNumberImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.mail.crypto.ToolSmimeException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMEUtil;
import org.bouncycastle.operator.OperatorCreationException;
import org.bouncycastle.util.Store;
import org.springframework.util.MimeType;

/**
 * Derived from:
 * <ul>
 * <li><a href="http://tools.ietf.org/html/rfc2046">RFC 2046 - Media Types</a></li>
 * <li><a href="http://tools.ietf.org/html/rfc5322">RFC 5322 - Internet Message Format</a></li>
 * <li><a href="http://tools.ietf.org/html/rfc5751">RFC 5751 - S/MIME 3.2 Message Specification</a></li>
 * </ul>
 * 
 * A summary is available here: <a href="http://en.wikipedia.org/wiki/S/MIME">S/MIME</a>
 */
public abstract class ToolSmimeUtils {
    @SuppressWarnings({ "unchecked" })
    public static Map<SignerInformation, CertificateInfo> verifySignatures(SMIMESigned signed) throws MessagingException {
        Store signedCerts = signed.getCertificates();
        JcaSimpleSignerInfoVerifierBuilder signerInfoVerifierBuilder = new JcaSimpleSignerInfoVerifierBuilder();
        Map<SignerId, SignerInformation> signerInfoMap = mapSigners(signed);
        Map<SignerInformation, CertificateInfo> signerCertInfoMap = new LinkedHashMap<>(signerInfoMap.size());
        SignerInformation signerInfo;

        for (SignerId signerId : signerInfoMap.keySet()) {
            signerInfo = signerInfoMap.get(signerId);

            for (X509CertificateHolder certHolder : ((Collection<X509CertificateHolder>) signedCerts.getMatches(signerId))) {
                try {
                    if (signerInfo.verify(signerInfoVerifierBuilder.build(certHolder))) {
                        signerCertInfoMap.put(signerInfo, new CertificateInfoImpl(CertificateUtils.CERT_CONV.getCertificate(certHolder)));

                        break;
                    }
                } catch (CertificateException | CMSException | OperatorCreationException e) {
                    throw new ToolSmimeException(String.format(
                        "Unable to verify mail signed data signer (id={issuer=%s, serialNum=%s}) certificate (subj={%s}).", signerId.getIssuer(),
                        new CertificateSerialNumberImpl(signerId.getSerialNumber()), certHolder.getSubject()));
                }
            }

            if (!signerCertInfoMap.containsKey(signerInfo)) {
                throw new ToolSmimeException(String.format("Unable to verify mail signed data signer (id={issuer=%s, serialNum=%s}).", signerId.getIssuer(),
                    new CertificateSerialNumberImpl(signerId.getSerialNumber())));
            }
        }

        return signerCertInfoMap;
    }

    @SuppressWarnings({ "unchecked" })
    public static Map<SignerId, SignerInformation> mapSigners(SMIMESigned signed) throws MessagingException {
        Collection<SignerInformation> signerInfos = ((Collection<SignerInformation>) signed.getSignerInfos().getSigners());
        Map<SignerId, SignerInformation> signerInfoMap = new LinkedHashMap<>(signerInfos.size());

        for (SignerInformation signerInfo : signerInfos) {
            // TODO: check signature content type
            // TODO: check signature digest algorithm

            signerInfoMap.put(signerInfo.getSID(), signerInfo);
        }

        return signerInfoMap;
    }

    public static SMIMESigned getSigned(ToolMimeMessageHelper msgHelper, MimeBodyPart bodyPart) throws MessagingException {
        try {
            MimeType bodyPartContentType = ToolMimePartUtils.getContentType(bodyPart);

            if (ToolSmimeContentTypeUtils.isMultipartSigned(bodyPartContentType)) {
                return new SMIMESigned(((MimeMultipart) bodyPart.getContent()));
            } else if (ToolSmimeContentTypeUtils.isSignedData(bodyPartContentType)) {
                return new SMIMESigned(bodyPart);
            } else {
                throw new ToolSmimeException(String.format(
                    "Decrypted mail MIME message (id=%s, from=%s, to=%s) body part content (type=%s) is not signed data.", msgHelper.getMimeMessage()
                        .getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), bodyPartContentType));
            }
        } catch (ToolSmimeException e) {
            throw e;
        } catch (CMSException | IOException | MessagingException | SMIMEException e) {
            throw new ToolSmimeException(String.format(
                "Unable to get signed data wrapper for mail MIME message (id=%s, from=%s, to=%s) body part content (type=%s).", msgHelper.getMimeMessage()
                    .getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), bodyPart.getContentType()), e);
        }
    }

    @Nullable
    public static MimeBodyPart decrypt(ToolMimeMessageHelper msgHelper, SMIMEEnveloped enveloped, PrivateKey privateKey, X509Certificate cert)
        throws MessagingException {
        Map<KeyTransRecipientId, KeyTransRecipientInformation> recipientInfoMap = mapRecipients(msgHelper, enveloped);
        KeyTransRecipientId recipientId = new JceKeyTransRecipientId(cert.getIssuerX500Principal(), cert.getSerialNumber());

        if (!recipientInfoMap.containsKey(recipientId)) {
            return null;
        }

        try {
            return SMIMEUtil.toMimeBodyPart(recipientInfoMap.get(recipientId).getContent(new JceKeyTransEnvelopedRecipient(privateKey)));
        } catch (CMSException | SMIMEException e) {
            throw new ToolSmimeException(String.format("Unable to decrypt mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s).", msgHelper
                .getMimeMessage().getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), ToolMimePartUtils.getContentType(enveloped.getEncryptedContent())), e);
        }
    }

    @SuppressWarnings({ "unchecked" })
    public static Map<KeyTransRecipientId, KeyTransRecipientInformation> mapRecipients(ToolMimeMessageHelper msgHelper, SMIMEEnveloped enveloped)
        throws MessagingException {
        Collection<KeyTransRecipientInformation> recipientInfos = ((Collection<KeyTransRecipientInformation>) enveloped.getRecipientInfos().getRecipients());
        Map<KeyTransRecipientId, KeyTransRecipientInformation> recipientInfoMap = new LinkedHashMap<>(recipientInfos.size());
        KeyTransRecipientId recipientId;
        X500Name recipientIssuer;
        BigInteger recipientSerialNum;

        for (KeyTransRecipientInformation recipientInfo : recipientInfos) {
            recipientIssuer = (recipientId = ((KeyTransRecipientId) recipientInfo.getRID())).getIssuer();
            recipientSerialNum = recipientId.getSerialNumber();

            try {
                recipientInfoMap.put(new JceKeyTransRecipientId(new X500Principal(recipientIssuer.getEncoded()), recipientSerialNum), recipientInfo);
            } catch (IOException e) {
                throw new ToolSmimeException(String.format(
                    "Unable to map mail MIME message (id=%s, from=%s, to=%s) enveloped content (type=%s) recipient (issuer={%s}, serialNum=%s).", msgHelper
                        .getMimeMessage().getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), ToolMimePartUtils.getContentType(enveloped
                        .getEncryptedContent()), recipientIssuer, new CertificateSerialNumberImpl(recipientSerialNum)), e);
            }
        }

        return recipientInfoMap;
    }

    public static SMIMEEnveloped getEnveloped(ToolMimeMessageHelper msgHelper) throws MessagingException {
        MimeMessage msg = msgHelper.getMimeMessage();

        try {
            MimeType msgContentType = msgHelper.getContentType();

            if (!ToolSmimeContentTypeUtils.isEnvelopedData(msgContentType)) {
                throw new ToolSmimeException(String.format("Mail MIME message (id=%s, from=%s, to=%s) content (type=%s) is not enveloped data.",
                    msg.getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), msgContentType));
            }

            return new SMIMEEnveloped(msg);

            // TODO: check cipher algorithm
        } catch (ToolSmimeException e) {
            throw e;
        } catch (CMSException | MessagingException e) {
            throw new ToolSmimeException(String.format("Unable to get enveloped data wrapper for mail MIME message (id=%s, from=%s, to=%s) content (type=%s).",
                msg.getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), msg.getContentType()), e);
        }
    }
}
