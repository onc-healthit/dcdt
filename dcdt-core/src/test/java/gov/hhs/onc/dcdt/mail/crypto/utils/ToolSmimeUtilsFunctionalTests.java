package gov.hhs.onc.dcdt.mail.crypto.utils;

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
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.crypto.MailEncryptionAlgorithm;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.util.MimeType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.func.testcases.discovery.all" }, groups = { "dcdt.test.func.mail.all", "dcdt.test.func.mail.crypto.all",
    "dcdt.test.func.mail.crypto.utils.all", "dcdt.test.func.mail.crypto.utils.smime" })
@DirtiesContext
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

    @Value("${dcdt.test.crypto.key.public.dts500}")
    private String testPublicKeyStr;

    @Value("${dcdt.test.crypto.key.private.dts500}")
    private String testPrivateKeyStr;

    @Value("${dcdt.test.crypto.cert.dts500}")
    private String testCertStr;

    @Value("${dcdt.test.lookup.domain.1.mail.addr.1}")
    private MailAddress testToAddr;

    @Value("${dcdt.test.mail.crypto.type.multipart.signed}")
    private MimeType testMultipartSignedMimeType;

    @Value("${dcdt.test.mail.crypto.type.pkcs7.mime.1}")
    private MimeType testPkcs7MimeContentType1;

    @Value("${dcdt.test.mail.crypto.type.pkcs7.mime.2}")
    private MimeType testPkcs7MimeContentType2;

    private CertificateInfo testCertInfo;
    private CredentialInfo testCredInfo;
    private DiscoveryTestcaseCredential testCred;
    private List<DiscoveryTestcase> testDiscoveryTestcases = new ArrayList<>();

    @Test(dependsOnMethods = { "testIsMultipartSigned" })
    public void testDecryptMailPkcs7MimeValid() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_valid.eml");
        assertDiscoveryTestcaseResultProperties(result, true, this.testCred);
    }

    @Test
    public void testDecryptMailPkcs7MimeEncryptedWithWrongKey() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_encryptedWithWrongKey.eml");
        assertDiscoveryTestcaseResultProperties(result, false, null);
    }

    @Test(dependsOnMethods = { "testMimeTypeParamOrder", "testIsMultipartSigned" })
    public void testDecryptMailPkcs7MimeDiffMimeTypeParamOrder() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_diffMimeTypeParamOrder.eml");
        assertDiscoveryTestcaseResultProperties(result, true, this.testCred);
    }

    @Test(enabled = false, dependsOnMethods = { "testIsMultipartSigned" })
    public void testDecryptMailXPkcs7MimeValid() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_valid.eml");
        assertDiscoveryTestcaseResultProperties(result, true, this.testCred);
    }

    @Test
    public void testDecryptMailXPkcsMimeNoMatchingKeys() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_noMatchingKeys.eml");
        assertDiscoveryTestcaseResultProperties(result, false, null);

        // noinspection ConstantConditions
        Assert.assertNotEquals(result.getMailInfo().getTo(), this.testToAddr);
    }

    @Test
    public void testDecryptMailInvalidMimeType() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_invalidMimeType.eml");
        assertDiscoveryTestcaseResultProperties(result, false, null);
    }

    @Test(dependsOnMethods = { "testMimeTypeParamOrder" })
    public void testIsMultipartSigned() {
        Assert.assertTrue(ToolSmimeContentTypeUtils.isMultipartSigned(this.testMultipartSignedMimeType));
    }

    @Test
    public void testMimeTypeParamOrder() {
        Assert.assertTrue(ToolMimeTypeUtils.equals(this.testPkcs7MimeContentType1, this.testPkcs7MimeContentType2));
    }

    @Test(dependsOnMethods = { "testIsMultipartSigned" })
    public void testDecryptSignedAndEncryptedMessage() throws IOException, MessagingException {
        String toAddr = this.testToAddr.toAddress();
        MimeMessage msg = new MimeMessage(this.mailSession);
        msg.setFrom(toAddr);
        msg.setRecipient(RecipientType.TO, this.testToAddr.toInternetAddress());
        msg.setSubject(toAddr);
        msg.setText(toAddr);
        msg.saveChanges();

        ToolMimeMessageHelper unencryptedMsgHelper = new ToolMimeMessageHelper(msg, this.mailEnc);
        ToolMimeMessageHelper encryptedMsgHelper =
            ToolSmimeUtils.signAndEncrypt(unencryptedMsgHelper, this.testCredInfo, this.testCertInfo, MailEncryptionAlgorithm.AES256);

        assertMessageHeadersMatch(unencryptedMsgHelper, encryptedMsgHelper);
        assertDiscoveryTestcaseResultProperties(processDiscoveryTestcaseSubmission(encryptedMsgHelper), true, this.testCred);
    }

    @BeforeClass(dependsOnMethods = { "buildCredentialInfo" })
    public void buildTestDiscoveryTestcases() {
        DiscoveryTestcase discoveryTestcase1 = (DiscoveryTestcase) this.applicationContext.getBean("discoveryTestcase1");
        DiscoveryTestcase discoveryTestcase2 = (DiscoveryTestcase) this.applicationContext.getBean("discoveryTestcase2");

        discoveryTestcase1.setMailAddress(this.testToAddr);
        this.testCred = discoveryTestcase1.getTargetCredentials().iterator().next();
        this.testCred.setCredentialInfo(this.testCredInfo);
        this.testDiscoveryTestcases.add(discoveryTestcase1);
        this.testDiscoveryTestcases.add(discoveryTestcase2);
    }

    @BeforeClass
    public void buildCredentialInfo() throws CryptographyException {
        KeyInfo testKeyInfo =
            new KeyInfoImpl(KeyUtils.readPublicKey(Base64.decodeBase64(this.testPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER), KeyUtils.readPrivateKey(
                Base64.decodeBase64(this.testPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
        this.testCertInfo =
            new CertificateInfoImpl(CertificateUtils.readCertificate(Base64.decodeBase64(this.testCertStr), CertificateType.X509, DataEncoding.DER));
        this.testCredInfo = new CredentialInfoImpl(testKeyInfo, testCertInfo);
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

    private DiscoveryTestcaseResult decryptAndParseEmail(String mailLoc) throws IOException, MessagingException {
        try (InputStream mailInStream = ToolResourceUtils.getInputStream(mailLoc)) {
            return processDiscoveryTestcaseSubmission(new ToolMimeMessageHelper(new MimeMessage(this.mailSession, mailInStream), this.mailEnc));
        }
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
            CollectionUtils.find(this.testDiscoveryTestcases, new DiscoveryTestcaseMailAddressPredicate(msgHelper.getTo())));
    }
}
