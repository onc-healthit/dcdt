package gov.hhs.onc.dcdt.mail.crypto.utils;

import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateSerialNumberImpl;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.mail.MailContentTransferEncoding;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.crypto.ToolSmimeException;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.math.BigInteger;
import java.security.PrivateKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateException;
import java.security.cert.X509Certificate;
import java.util.ArrayList;
import java.util.Collection;
import java.util.EnumSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import javax.mail.MessagingException;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import javax.security.auth.x500.X500Principal;
import org.bouncycastle.asn1.ASN1EncodableVector;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.cms.AttributeTable;
import org.bouncycastle.asn1.smime.SMIMECapabilitiesAttribute;
import org.bouncycastle.asn1.smime.SMIMECapabilityVector;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.cert.X509CertificateHolder;
import org.bouncycastle.cert.jcajce.JcaCertStore;
import org.bouncycastle.cms.CMSException;
import org.bouncycastle.cms.KeyTransRecipientId;
import org.bouncycastle.cms.KeyTransRecipientInformation;
import org.bouncycastle.cms.SignerId;
import org.bouncycastle.cms.SignerInformation;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoGeneratorBuilder;
import org.bouncycastle.cms.jcajce.JcaSimpleSignerInfoVerifierBuilder;
import org.bouncycastle.cms.jcajce.JceCMSContentEncryptorBuilder;
import org.bouncycastle.cms.jcajce.JceKeyTransEnvelopedRecipient;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientId;
import org.bouncycastle.cms.jcajce.JceKeyTransRecipientInfoGenerator;
import org.bouncycastle.mail.smime.SMIMEEnveloped;
import org.bouncycastle.mail.smime.SMIMEEnvelopedGenerator;
import org.bouncycastle.mail.smime.SMIMEException;
import org.bouncycastle.mail.smime.SMIMESigned;
import org.bouncycastle.mail.smime.SMIMESignedGenerator;
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
 * <li><a href="http://wiki.directproject.org/Applicability+Statement+for+Secure+Health+Transport+Working+Version">Applicability Statement for Secure Health
 * Transport</a></li>
 * </ul>
 * 
 * A summary is available here: <a href="http://en.wikipedia.org/wiki/S/MIME">S/MIME</a>
 */
public abstract class ToolSmimeUtils {
    @SuppressWarnings({ "unchecked" })
    public static Map<SignerId, CertificateInfo> verifySignatures(SMIMESigned signed) throws MessagingException {
        Store signedCerts = signed.getCertificates();
        JcaSimpleSignerInfoVerifierBuilder signerInfoVerifierBuilder = new JcaSimpleSignerInfoVerifierBuilder();
        Map<SignerId, SignerInformation> signerInfoMap = mapSigners(signed);
        Map<SignerId, CertificateInfo> signerCertMap = new LinkedHashMap<>(signerInfoMap.size());
        SignerInformation signerInfo;

        for (SignerId signerId : signerInfoMap.keySet()) {
            signerInfo = signerInfoMap.get(signerId);

            for (X509CertificateHolder certHolder : ((Collection<X509CertificateHolder>) signedCerts.getMatches(signerId))) {
                try {
                    signerCertMap.put(signerId, new CertificateInfoImpl(CertificateUtils.CERT_CONV.getCertificate(certHolder)));
                } catch (CertificateException e) {
                    throw new ToolSmimeException(String.format("Unable to verify mail signed data signer (id={issuer=%s, serialNum=%s}).",
                        signerId.getIssuer(), new CertificateSerialNumberImpl(signerId.getSerialNumber())), e);
                }

                // @formatter:off
                /*
                try {
                    if (signerInfo.verify(signerInfoVerifierBuilder.build(certHolder))) {
                        signerCertMap.put(signerId, new CertificateInfoImpl(CertificateUtils.CERT_CONV.getCertificate(certHolder)));

                        break;
                    }
                } catch (CertificateException | CMSException | OperatorCreationException e) {
                    throw new ToolSmimeException(String.format(
                        "Unable to verify mail signed data signer (id={issuer=%s, serialNum=%s}) certificate (subj={%s}).", signerId.getIssuer(),
                        new CertificateSerialNumberImpl(signerId.getSerialNumber()), certHolder.getSubject()), e);
                }
                */
                // @formatter:on
            }

            if (!signerCertMap.containsKey(signerId)) {
                throw new ToolSmimeException(String.format("Unable to verify mail signed data signer (id={issuer=%s, serialNum=%s}).", signerId.getIssuer(),
                    new CertificateSerialNumberImpl(signerId.getSerialNumber())));
            }
        }

        return signerCertMap;
    }

    @SuppressWarnings({ "unchecked" })
    public static Map<SignerId, SignerInformation> mapSigners(SMIMESigned signed) throws MessagingException {
        Collection<SignerInformation> signerInfos = ((Collection<SignerInformation>) signed.getSignerInfos().getSigners());
        Map<SignerId, SignerInformation> signerInfoMap = new LinkedHashMap<>(signerInfos.size());

        for (SignerInformation signerInfo : signerInfos) {
            signerInfoMap.put(signerInfo.getSID(), signerInfo);
        }

        return signerInfoMap;
    }

    public static SMIMESigned getSigned(ToolMimeMessageHelper msgHelper, MimeBodyPart bodyPart) throws MessagingException {
        try {
            MimeType bodyPartContentType = ToolMimePartUtils.getContentType(bodyPart);

            if (ToolSmimeContentTypeUtils.getMicalg(bodyPartContentType) == null) {
                // noinspection ConstantConditions
                throw new ToolSmimeException(
                    String
                        .format(
                            "Mail MIME message (id=%s, from=%s, to=%s) signed data content type (type=%s) has unknown/invalid Message Integrity Check algorithm (micalg) value: %s",
                            msgHelper.getMimeMessage().getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), bodyPartContentType,
                            bodyPartContentType.getParameter(MailContentTypes.MICALG_PARAM_NAME)));
            }

            SMIMESigned signed;

            if (ToolSmimeContentTypeUtils.isMultipartSigned(bodyPartContentType)) {
                MimeMultipart bodyMultipart = ((MimeMultipart) bodyPart.getContent());
                MimeType sigPartContentType = ToolMimePartUtils.getContentType(((MimeBodyPart) bodyMultipart.getBodyPart(1)));

                if (!ToolSmimeContentTypeUtils.isDetachedSignature(sigPartContentType)) {
                    throw new ToolSmimeException(String.format(
                        "Mail MIME message (id=%s, from=%s, to=%s) signed data signature body part content (type=%s) is not a detached signature.", msgHelper
                            .getMimeMessage().getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), sigPartContentType));
                }

                signed = new SMIMESigned(bodyMultipart);
            } else if (ToolSmimeContentTypeUtils.isSignedData(bodyPartContentType)) {
                signed = new SMIMESigned(bodyPart);

                if (ToolMimePartUtils.getContentXferEncoding(bodyPart) != MailContentTransferEncoding.BASE64) {
                    throw new ToolSmimeException(String.format(
                        "Mail MIME message (id=%s, from=%s, to=%s) signed data content (type=%s) transfer encoding is not base64: %s", msgHelper
                            .getMimeMessage().getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), bodyPartContentType, bodyPart.getEncoding()));
                }
            } else {
                throw new ToolSmimeException(String.format(
                    "Decrypted mail MIME message (id=%s, from=%s, to=%s) body part content (type=%s) is not signed data.", msgHelper.getMimeMessage()
                        .getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), bodyPartContentType));
            }

            return signed;
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

            SMIMEEnveloped enveloped = new SMIMEEnveloped(msg);
            ASN1ObjectIdentifier encAlgOid = new ASN1ObjectIdentifier(enveloped.getEncryptionAlgOID());

            if (CryptographyUtils.findObjectId(MailEncryptionAlgorithm.class, encAlgOid) == null) {
                throw new ToolSmimeException(String.format(
                    "Mail MIME message (id=%s, from=%s, to=%s) content (type=%s) has unknown/invalid content encryption algorithm: oid=%s", msg.getMessageID(),
                    msgHelper.getFrom(), msgHelper.getTo(), msgContentType, encAlgOid.getId()));
            }

            if (msgHelper.getContentXferEncoding() != MailContentTransferEncoding.BASE64) {
                throw new ToolSmimeException(String.format(
                    "Mail MIME message (id=%s, from=%s, to=%s) enveloped data content (type=%s) transfer encoding is not base64: %s", msg.getMessageID(),
                    msgHelper.getFrom(), msgHelper.getTo(), msgContentType, msg.getEncoding()));
            }

            return enveloped;
        } catch (ToolSmimeException e) {
            throw e;
        } catch (CMSException | MessagingException e) {
            throw new ToolSmimeException(String.format("Unable to get enveloped data wrapper for mail MIME message (id=%s, from=%s, to=%s) content (type=%s).",
                msg.getMessageID(), msgHelper.getFrom(), msgHelper.getTo(), msg.getContentType()), e);
        }
    }

    public static MimeBodyPart encrypt(MimeBodyPart unencryptedBodyPart, X509Certificate cert, MailEncryptionAlgorithm encryptionAlg) throws MessagingException {
        MimeType bodyPartContentType = ToolMimePartUtils.getContentType(unencryptedBodyPart);

        try {
            SMIMEEnvelopedGenerator envelopedGen = new SMIMEEnvelopedGenerator();
            envelopedGen.addRecipientInfoGenerator(new JceKeyTransRecipientInfoGenerator(cert));
            JceCMSContentEncryptorBuilder encryptorBuilder = new JceCMSContentEncryptorBuilder(encryptionAlg.getOid());

            if (ToolSmimeContentTypeUtils.isSignedData(bodyPartContentType)) {
                return envelopedGen.generate(unencryptedBodyPart, encryptorBuilder.build());
            } else {
                MimeMultipart multipartBody = (MimeMultipart) unencryptedBodyPart.getContent();
                MimeType multipartContentType = ToolMimePartUtils.getContentType(multipartBody);

                if (ToolSmimeContentTypeUtils.isMultipartSigned(multipartContentType)) {
                    return envelopedGen.generate(unencryptedBodyPart, encryptorBuilder.build());
                } else {
                    throw new ToolSmimeException(
                        String
                            .format(
                                "Content (type=%s) of MIME body part (class=%s), content (type=%s) of MIME multipart (class=%s) is not of a signed-data type=(%s or %s) or of a multipart/signed type=(%s or %s).",
                                bodyPartContentType, ToolClassUtils.getName(unencryptedBodyPart), multipartContentType, ToolClassUtils.getName(multipartBody),
                                MailContentTypes.APP_PKCS7_MIME_SIGNED, MailContentTypes.APP_X_PKCS7_MIME_SIGNED,
                                MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_X_PKCS7_SIG));
                }
            }
        } catch (CMSException | SMIMEException | CertificateEncodingException | IOException e) {
            throw new ToolSmimeException(String.format("Unable to encrypt content (type=%s) of MIME body part (class=%s).", bodyPartContentType,
                ToolClassUtils.getName(unencryptedBodyPart)), e);
        }
    }

    public static MimeMultipart sign(ToolMimeMessageHelper unsignedMsgHelper, PrivateKey privateKey, X509Certificate cert) throws MessagingException {
        MimeMessage unsignedMsg = unsignedMsgHelper.getMimeMessage();
        CertificateInfo certInfo = new CertificateInfoImpl(cert);
        SignatureAlgorithm sigAlg = certInfo.getSignatureAlgorithm();

        if (sigAlg == null) {
            throw new ToolSmimeException(String.format(
                "Unable to find a signature algorithm for signing the MIME message (id=%s, from=%s, to=%s) content (type=%s).", unsignedMsg.getMessageID(),
                unsignedMsgHelper.getFrom(), unsignedMsgHelper.getTo(), unsignedMsg.getContentType()));
        } else {
            SMIMESignedGenerator signer = new SMIMESignedGenerator();
            ASN1EncodableVector signedAttrs = getSignedAttributes();

            List<X509Certificate> certList = new ArrayList<>();
            certList.add(cert);

            try {
                signer.addCertificates(new JcaCertStore(certList));
                signer.addSignerInfoGenerator(new JcaSimpleSignerInfoGeneratorBuilder().setSignedAttributeGenerator(new AttributeTable(signedAttrs)).build(
                    sigAlg.getId(), privateKey, cert));

                return signer.generate(unsignedMsg);
            } catch (OperatorCreationException | CertificateEncodingException | SMIMEException e) {
                throw new ToolSmimeException(String.format("Unable to sign MIME message (id=%s, from=%s, to=%s) content (type=%s).",
                    unsignedMsg.getMessageID(), unsignedMsgHelper.getFrom(), unsignedMsgHelper.getTo(), unsignedMsg.getContentType()), e);
            }
        }
    }

    public static ASN1EncodableVector getSignedAttributes() {
        ASN1EncodableVector signedAttrs = new ASN1EncodableVector();
        SMIMECapabilityVector caps = new SMIMECapabilityVector();

        for (MailEncryptionAlgorithm alg : EnumSet.allOf(MailEncryptionAlgorithm.class)) {
            caps.addCapability(alg.getOid());
        }

        signedAttrs.add(new SMIMECapabilitiesAttribute(caps));

        return signedAttrs;
    }

    public static ToolMimeMessageHelper signAndEncrypt(ToolMimeMessageHelper msgHelper, CredentialInfo signerCredInfo, CertificateInfo encryptionCertInfo,
        MailEncryptionAlgorithm encryptionAlg) throws MessagingException, IOException {
        MimeMessage msg = msgHelper.getMimeMessage();
        MimeBodyPart signedBodyPart = new MimeBodyPart();
        // noinspection ConstantConditions
        signedBodyPart.setContent(ToolSmimeUtils.sign(msgHelper, signerCredInfo.getKeyDescriptor().getPrivateKey(), signerCredInfo.getCertificateDescriptor()
            .getCertificate()));

        MimeBodyPart encryptedBodyPart = ToolSmimeUtils.encrypt(signedBodyPart, encryptionCertInfo.getCertificate(), encryptionAlg);
        MimeMessage encryptedMsg = new MimeMessage(msg.getSession());
        encryptedMsg.setContent(encryptedBodyPart.getContent(), encryptedBodyPart.getContentType());
        encryptedMsg.saveChanges();

        return setMessageHeaders(encryptedMsg, msgHelper);
    }

    public static ToolMimeMessageHelper setMessageHeaders(MimeMessage encryptedMsg, ToolMimeMessageHelper msgHelper) throws MessagingException, IOException {
        ToolMimeMessageHelper encryptedMsgHelper = new ToolMimeMessageHelper(encryptedMsg, msgHelper.getMailEncoding());
        encryptedMsgHelper.setFrom(msgHelper.getFrom());
        encryptedMsgHelper.setTo(msgHelper.getTo());
        encryptedMsgHelper.setSubject(msgHelper.getSubject());

        return encryptedMsgHelper;
    }
}
