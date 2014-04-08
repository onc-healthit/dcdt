package gov.hhs.onc.dcdt.mail.crypto.utils;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.config.instance.InstanceConfig;
import gov.hhs.onc.dcdt.config.instance.InstanceConfigRegistry;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.certs.CertificateInfo;
import gov.hhs.onc.dcdt.crypto.certs.CertificateType;
import gov.hhs.onc.dcdt.crypto.certs.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.crypto.keys.impl.KeyInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.MailHeaderNames;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMimePartUtils;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import java.io.IOException;
import java.net.InetAddress;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;
import javax.annotation.Resource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.comparators.FixedOrderComparator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MimeType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;
import org.xbill.DNS.Name;

@Test(dependsOnGroups = { "dcdt.test.func.testcases.discovery.all" }, groups = { "dcdt.test.func.mail.all", "dcdt.test.func.mail.crypto.all",
    "dcdt.test.func.mail.crypto.utils.all", "dcdt.test.func.mail.crypto.utils.smime" })
public class ToolSmimeUtilsFunctionalTests extends AbstractToolFunctionalTests {
    @Resource(name = "mailSessionPlain")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Session mailSession;

    @Resource(name = "charsetUtf8")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private Charset mailEnc;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseProcessor testcaseProcessor;

    @Value("${dcdt.test.instance.domain.name}")
    private Name testInstanceConfigDomainName;

    @Value("${dcdt.test.instance.ip.addr}")
    private InetAddress testInstanceConfigIpAddr;

    @Value("${dcdt.test.crypto.key.public.dts500}")
    private String testPublicKeyStr;

    @Value("${dcdt.test.crypto.key.private.dts500}")
    private String testPrivateKeyStr;

    @Value("${dcdt.test.crypto.cert.dts500}")
    private String testCertStr;

    private MailAddress testToAddr;

    @Value("${dcdt.test.discovery.mail.mapping.results.addr}")
    private MailAddress testToAddr2;

    @Value("${dcdt.test.lookup.domain.1.mail.addr.1}")
    private MailAddress testFromAddr;

    @Value("${dcdt.test.mail.crypto.type.multipart.signed}")
    private MimeType testMultipartSignedMimeType;

    @Value("${dcdt.test.mail.crypto.type.pkcs7.mime.1}")
    private MimeType testPkcs7MimeContentType1;

    @Value("${dcdt.test.mail.crypto.type.pkcs7.mime.2}")
    private MimeType testPkcs7MimeContentType2;

    private CredentialInfo testSignerCredInfo;
    private DiscoveryTestcaseCredential testTargetCred;
    private DiscoveryTestcaseCredential testBackgroundCred;
    private CertificateInfo testTargetCertInfo;
    private CertificateInfo testBackgroundCertInfo;
    private List<DiscoveryTestcase> discoveryTestcases;

    @Test(dependsOnMethods = { "testIsMultipartSigned" })
    public void testDecryptMailPkcs7MimeValid() throws IOException, MessagingException {
        ToolMimeMessageHelper encryptedMsgHelper =
            createSignedAndEncryptedMessage(this.testToAddr, this.testFromAddr, this.testSignerCredInfo, this.testTargetCertInfo);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), true, this.testTargetCred);
    }

    @Test
    public void testDecryptMailPkcs7MimeEncryptedWithWrongCertificate() throws IOException, MessagingException {
        ToolMimeMessageHelper encryptedMsgHelper =
            createSignedAndEncryptedMessage(this.testToAddr, this.testFromAddr, this.testSignerCredInfo, this.testBackgroundCertInfo);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), false, this.testBackgroundCred);
    }

    @Test(dependsOnMethods = { "testMimeTypeParamOrder", "testIsMultipartSigned" })
    public void testDecryptMailPkcs7MimeDiffMimeTypeParamOrder() throws IOException, MessagingException {
        ToolMimeMessageHelper encryptedMsgHelper =
            createSignedAndEncryptedMessage(this.testToAddr, this.testFromAddr, this.testSignerCredInfo, this.testTargetCertInfo,
                MailEncryptionAlgorithm.AES256, MailContentTypes.APP_PKCS7_SIG_BASETYPE, MailContentTypes.APP_PKCS7_MIME_BASETYPE, true);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), true, this.testTargetCred);
    }

    @Test(dependsOnMethods = { "testIsMultipartSigned" })
    public void testDecryptMailXPkcs7MimeValid() throws IOException, MessagingException {
        ToolMimeMessageHelper encryptedMsgHelper =
            createSignedAndEncryptedMessage(this.testToAddr, this.testFromAddr, this.testSignerCredInfo, this.testTargetCertInfo,
                MailEncryptionAlgorithm.AES256, MailContentTypes.APP_X_PKCS7_SIG_BASETYPE, MailContentTypes.APP_X_PKCS7_MIME_BASETYPE, false);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), true, this.testTargetCred);
    }

    @Test
    public void testDecryptMailPkcsMimeInvalidTestcaseAddress() throws IOException, MessagingException {
        ToolMimeMessageHelper encryptedMsgHelper =
            createSignedAndEncryptedMessage(this.testToAddr2, this.testFromAddr, this.testSignerCredInfo, this.testTargetCertInfo);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), false, null);
    }

    @Test
    public void testDecryptMailInvalidMimeType() throws IOException, MessagingException {
        ToolMimeMessageHelper unencryptedMsgHelper = new ToolMimeMessageHelper(createMimeMessage(this.testToAddr, this.testFromAddr), this.mailEnc);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(unencryptedMsgHelper), false, null);
    }

    @Test(dependsOnMethods = { "testMimeTypeParamOrder" })
    public void testIsMultipartSigned() {
        Assert.assertTrue(ToolSmimeContentTypeUtils.isMultipartSigned(this.testMultipartSignedMimeType));
    }

    @Test
    public void testMimeTypeParamOrder() {
        Assert.assertTrue(ToolMimeTypeUtils.equals(this.testPkcs7MimeContentType1, this.testPkcs7MimeContentType2));
    }

    @BeforeClass(dependsOnMethods = { "registerInstanceConfig" })
    public void setupEncryptionCredentialInfo() {
        this.discoveryTestcases = ToolBeanFactoryUtils.getBeansOfType(this.applicationContext, DiscoveryTestcase.class);
        DiscoveryTestcase discoveryTestcase1 = this.discoveryTestcases.get(0);
        this.testToAddr = discoveryTestcase1.getMailAddress();

        this.testTargetCred = discoveryTestcase1.getTargetCredentials().iterator().next();
        // noinspection ConstantConditions
        this.testTargetCertInfo = this.testTargetCred.getCredentialInfo().getCertificateDescriptor();
        this.testBackgroundCred = discoveryTestcase1.getBackgroundCredentials().iterator().next();
        // noinspection ConstantConditions
        this.testBackgroundCertInfo = this.testBackgroundCred.getCredentialInfo().getCertificateDescriptor();
    }

    @BeforeClass
    public void registerInstanceConfig() {
        InstanceConfig instanceConfig = ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfig.class);
        // noinspection ConstantConditions
        instanceConfig.setDomainName(this.testInstanceConfigDomainName);
        instanceConfig.setIpAddress(this.testInstanceConfigIpAddr);
        // noinspection ConstantConditions
        ToolBeanFactoryUtils.getBeanOfType(this.applicationContext, InstanceConfigRegistry.class).registerBeans(instanceConfig);
    }

    @BeforeClass
    public void buildSignerCredentialInfo() throws CryptographyException {
        KeyInfo testSignerKeyInfo =
            new KeyInfoImpl(KeyUtils.readPublicKey(Base64.decodeBase64(this.testPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER), KeyUtils.readPrivateKey(
                Base64.decodeBase64(this.testPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
        CertificateInfo testSignerCertInfo =
            new CertificateInfoImpl(CertificateUtils.readCertificate(Base64.decodeBase64(this.testCertStr), CertificateType.X509, DataEncoding.DER));
        this.testSignerCredInfo = new CredentialInfoImpl(testSignerKeyInfo, testSignerCertInfo);
    }

    private void assertDiscoveryTestcaseResultProperties(DiscoveryTestcaseResult result, boolean successful, DiscoveryTestcaseCredential credFound)
        throws MessagingException {
        MailInfo mailInfo = result.getMailInfo();

        Assert.assertNotNull(
            mailInfo,
            String.format("Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}) does not contain mail information.",
                result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound()));
        Assert.assertNotNull(
            mailInfo.getFrom(),
            String.format("Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}) mail information does not have a from address.",
                result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound()));
        Assert.assertNotNull(
            mailInfo.getTo(),
            String.format("Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}) mail information does not have a to address.",
                result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound()));
        Assert.assertTrue(mailInfo.hasMessageHelper(), String.format(
            "Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}, errorMsg=%s) mail MIME message was incorrectly processed.",
            result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound(), mailInfo.getDecryptionErrorMessage()));
        Assert.assertEquals(
            result.isSuccessful(),
            successful,
            String.format("Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}, errorMsg=%s) outcomes do not match.",
                result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound(), mailInfo.getDecryptionErrorMessage()));
        Assert.assertEquals(
            result.getCredentialFound(),
            credFound,
            String.format("Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}, errorMsg=%s) found credentials do not match.",
                result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound(), mailInfo.getDecryptionErrorMessage()));
        // noinspection ConstantConditions
        Assert.assertEquals(result.hasTestcase(), mailInfo.getTo().equals(this.testToAddr), String.format(
            "Discovery testcase result (successful=%s, credExpected={%s}, credFound={%s}, errorMsg=%s) association was incorrectly determined.",
            result.isSuccessful(), result.getCredentialExpected(), result.getCredentialFound(), mailInfo.getDecryptionErrorMessage()));

        if (successful) {
            Assert.assertEquals(result.getCredentialFound(), result.getCredentialExpected(),
                String.format("Discovery testcase result (msg=%s) credentials do not match.", mailInfo.getDecryptionErrorMessage()));
            Assert.assertFalse(mailInfo.hasDecryptionErrorMessage(),
                String.format("Successful Discovery testcase result has decryption error message: %s", mailInfo.getDecryptionErrorMessage()));
        }
    }

    private ToolMimeMessageHelper createSignedAndEncryptedMessage(MailAddress to, MailAddress from, CredentialInfo signerCredInfo,
        CertificateInfo encryptionCertInfo) throws MessagingException, IOException {
        return createSignedAndEncryptedMessage(to, from, signerCredInfo, encryptionCertInfo, MailEncryptionAlgorithm.AES256);
    }

    private ToolMimeMessageHelper createSignedAndEncryptedMessage(MailAddress to, MailAddress from, CredentialInfo signerCredInfo,
        CertificateInfo encryptionCertInfo, MailEncryptionAlgorithm encryptionAlg) throws MessagingException, IOException {
        MimeMessage msg = createMimeMessage(to, from);
        ToolMimeMessageHelper unencryptedMsgHelper = new ToolMimeMessageHelper(msg, this.mailEnc);
        ToolMimeMessageHelper encryptedMsgHelper = ToolSmimeUtils.signAndEncrypt(unencryptedMsgHelper, signerCredInfo, encryptionCertInfo, encryptionAlg);

        assertMessageHeadersMatch(unencryptedMsgHelper, encryptedMsgHelper);

        return encryptedMsgHelper;
    }

    private ToolMimeMessageHelper createSignedAndEncryptedMessage(MailAddress to, MailAddress from, CredentialInfo signerCredInfo,
        CertificateInfo encryptionCertInfo, MailEncryptionAlgorithm encryptionAlg, String sigBaseType, String envBaseType, boolean reorderParams)
        throws MessagingException, IOException {
        MimeMessage msg = createMimeMessage(to, from);
        ToolMimeMessageHelper unencryptedMsgHelper = new ToolMimeMessageHelper(msg, this.mailEnc);
        ToolMimeMessageHelper encryptedMsgHelper =
            signAndEncrypt(unencryptedMsgHelper, signerCredInfo, encryptionCertInfo, encryptionAlg, sigBaseType, envBaseType, reorderParams);

        assertMessageHeadersMatch(unencryptedMsgHelper, encryptedMsgHelper);

        return encryptedMsgHelper;
    }

    private MimeMessage createMimeMessage(MailAddress to, MailAddress from) throws MessagingException {
        String toAddr = to.toAddress();
        MimeMessage msg = new MimeMessage(this.mailSession);
        msg.setRecipient(RecipientType.TO, to.toInternetAddress());
        msg.setFrom(from.toAddress());
        msg.setSubject(toAddr);
        msg.setText(toAddr);
        msg.saveChanges();

        return msg;
    }

    private static ToolMimeMessageHelper signAndEncrypt(ToolMimeMessageHelper msgHelper, CredentialInfo signerCredInfo, CertificateInfo encryptionCertInfo,
        MailEncryptionAlgorithm encryptionAlg, String sigBaseType, String envBaseType, boolean reorderParams) throws MessagingException, IOException {
        MimeMessage msg = msgHelper.getMimeMessage();
        MimeBodyPart signedBodyPart = new MimeBodyPart();
        // noinspection ConstantConditions
        signedBodyPart.setContent(getSignedMultipartForBaseType(
            ToolSmimeUtils.sign(msgHelper, signerCredInfo.getKeyDescriptor().getPrivateKey(), signerCredInfo.getCertificateDescriptor().getCertificate()),
            sigBaseType));

        MimeBodyPart encryptedBodyPart = ToolSmimeUtils.encrypt(signedBodyPart, encryptionCertInfo.getCertificate(), encryptionAlg);
        MimeMessage encryptedMsg = new MimeMessage(msg.getSession());
        encryptedMsg.setContent(encryptedBodyPart.getContent(), updateEncryptedBodyPartContentType(envBaseType, reorderParams, encryptedBodyPart));
        encryptedMsg.saveChanges();

        return ToolSmimeUtils.setMessageHeaders(encryptedMsg, msgHelper);
    }

    private static MimeMultipart getSignedMultipartForBaseType(MimeMultipart signedMultipart, String sigBaseType) throws MessagingException {
        MimeBodyPart sigPart = (MimeBodyPart) signedMultipart.getBodyPart(1);
        sigPart.setHeader(MailHeaderNames.HEADER_NAME_CONTENT_TYPE,
            sigPart.getContentType().replace(ToolMimeTypeUtils.getBaseType(ToolMimePartUtils.getContentType(sigPart)), sigBaseType));

        // noinspection ConstantConditions
        String signedMultipartContentType =
            signedMultipart.getContentType().replace(
                ToolMimePartUtils.getContentType(signedMultipart).getParameters().get(MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PARAM_NAME),
                ToolStringUtils.quote(sigBaseType));

        return new MimeMultipart(signedMultipartContentType.substring(MailContentTypes.MULTIPART_TYPE.length() + 1), signedMultipart.getBodyPart(0), sigPart);
    }

    private static String updateEncryptedBodyPartContentType(String envBaseType, boolean reorderParams, MimeBodyPart encryptedBodyPart)
        throws MessagingException {
        MimeType encryptedContentType = ToolMimePartUtils.getContentType(encryptedBodyPart);
        String encryptedBaseType = ToolMimeTypeUtils.getBaseType(encryptedContentType);

        // noinspection ConstantConditions
        return reorderParams ? new MimeType(encryptedContentType.getType(), encryptedContentType.getSubtype(), reverseParameterOrder(encryptedContentType))
            .toString().replace(encryptedBaseType, envBaseType) : encryptedBodyPart.getContentType().replace(encryptedBaseType, envBaseType);
    }

    private static Map<String, String> reverseParameterOrder(MimeType mimeType) {
        Map<String, String> params = mimeType.getParameters();

        return ToolMapUtils.putAll(
            new TreeMap<String, String>(new FixedOrderComparator<>(ToolListUtils.reverse(ToolCollectionUtils.addAll(new ArrayList<String>(params.size()),
                params.keySet())))), params.entrySet());
    }

    private void assertMessageHeadersMatch(ToolMimeMessageHelper msgHelper1, ToolMimeMessageHelper msgHelper2) throws MessagingException {
        Assert.assertEquals(msgHelper1.getFrom(), msgHelper2.getFrom(),
            String.format("MIME message address (from=%s) does not match address (from=%s).", msgHelper1.getFrom(), msgHelper2.getFrom()));
        Assert.assertEquals(msgHelper1.getTo(), msgHelper2.getTo(),
            String.format("MIME message address (to=%s) does not match address (to=%s).", msgHelper1.getTo(), msgHelper2.getTo()));
        Assert.assertEquals(msgHelper1.getSubject(), msgHelper2.getSubject(),
            String.format("MIME message subject=%s does not match subject=%s.", msgHelper1.getSubject(), msgHelper2.getSubject()));
    }

    private DiscoveryTestcaseResult processDiscoveryTestcaseSubmission(ToolMimeMessageHelper msgHelper) throws MessagingException {
        return this.testcaseProcessor.process(msgHelper,
            CollectionUtils.find(this.discoveryTestcases, new DiscoveryTestcaseMailAddressPredicate(msgHelper.getTo())));
    }
}
