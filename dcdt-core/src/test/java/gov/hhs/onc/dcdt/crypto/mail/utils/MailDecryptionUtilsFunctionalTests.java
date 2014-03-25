package gov.hhs.onc.dcdt.crypto.mail.utils;

import gov.hhs.onc.dcdt.beans.utils.ToolBeanPropertyUtils;
import gov.hhs.onc.dcdt.beans.utils.ToolBeanUtils;
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
import gov.hhs.onc.dcdt.mail.MailContentTypes;
import gov.hhs.onc.dcdt.mail.MailInfo;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.ToolMimeMessageHelper;
import gov.hhs.onc.dcdt.mail.utils.ToolMailContentTypeUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase.DiscoveryTestcaseMailAddressPredicate;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredentialType;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.impl.DiscoveryTestcaseCredentialImpl;
import gov.hhs.onc.dcdt.testcases.discovery.impl.DiscoveryTestcaseImpl;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseProcessor;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.net.mime.utils.ToolMimeTypeUtils;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;
import javax.annotation.Resource;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.MimeMessage;
import org.apache.commons.codec.binary.Base64;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.MimeType;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.func.crypto.all", "dcdt.test.func.crypto.mail.all", "dcdt.test.func.crypto.mail.utils.all",
    "dcdt.test.func.crypto.mail.utils.decrypt" })
public class MailDecryptionUtilsFunctionalTests extends AbstractToolFunctionalTests {
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

    @Value("${dcdt.test.crypto.mail.type.multipart.signed}")
    private MimeType testMultipartSignedMimeType;

    @Value("${dcdt.test.crypto.mail.type.pkcs7.mime.1}")
    private MimeType testPkcs7MimeContentType1;

    @Value("${dcdt.test.crypto.mail.type.pkcs7.mime.2}")
    private MimeType testPkcs7MimeContentType2;

    private DiscoveryTestcaseCredential testCred1 = new DiscoveryTestcaseCredentialImpl();
    private List<DiscoveryTestcase> testDiscoveryTestcases = new ArrayList<>();

    @BeforeClass
    public void buildCredentialInfo() throws CryptographyException {
        KeyInfo testKeyInfo =
            new KeyInfoImpl(KeyUtils.readPublicKey(Base64.decodeBase64(this.testPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER), KeyUtils.readPrivateKey(
                Base64.decodeBase64(this.testPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
        CertificateInfo testCertInfo =
            new CertificateInfoImpl(CertificateUtils.readCertificate(Base64.decodeBase64(this.testCertStr), CertificateType.X509, DataEncoding.DER));
        CredentialInfo testCredInfo = new CredentialInfoImpl(testKeyInfo, testCertInfo);
        buildTestDiscoveryTestcases(testCredInfo);
    }

    private void buildTestDiscoveryTestcases(CredentialInfo credInfo) {
        DiscoveryTestcase discoveryTestcase1 = (DiscoveryTestcase) this.applicationContext.getBean("discoveryTestcase1");
        DiscoveryTestcase testDiscoveryTestcase1 = new DiscoveryTestcaseImpl();
        DiscoveryTestcase testDiscoveryTestcase2 = new DiscoveryTestcaseImpl();

        ToolBeanPropertyUtils.copy(ToolBeanUtils.wrap(discoveryTestcase1), ToolBeanUtils.wrap(testDiscoveryTestcase1));
        List<DiscoveryTestcaseCredential> targetCreds = new ArrayList<>();
        DiscoveryTestcaseCredential testCred2 = new DiscoveryTestcaseCredentialImpl();
        this.testCred1.setCredentialInfo(credInfo);
        this.testCred1.setType(DiscoveryTestcaseCredentialType.TARGET);
        testCred2.setType(DiscoveryTestcaseCredentialType.BACKGROUND);
        targetCreds.add(this.testCred1);
        targetCreds.add(testCred2);
        testDiscoveryTestcase1.setCredentials(targetCreds);
        testDiscoveryTestcase1.setMailAddress(this.testToAddr);

        ToolBeanPropertyUtils.copy(ToolBeanUtils.wrap(discoveryTestcase1), ToolBeanUtils.wrap(testDiscoveryTestcase2));
        this.testDiscoveryTestcases.add(testDiscoveryTestcase1);
        this.testDiscoveryTestcases.add(testDiscoveryTestcase2);
    }

    @Test(dependsOnMethods = { "testIsMultipartSignature" })
    public void testDecryptMailPkcs7MimeValid() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_valid.eml");
        assertDiscoveryTestcaseResultProperties(result, true, true, this.testCred1);
    }

    @Test
    public void testDecryptMailPkcs7MimeEncryptedWithWrongKey() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_encryptedWithWrongKey.eml");
        assertDiscoveryTestcaseResultProperties(result, true, false, null);
    }

    @Test(dependsOnMethods = { "testMimeTypeParamOrder", "testIsMultipartSignature" })
    public void testDecryptMailPkcs7MimeDiffMimeTypeParamOrder() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_diffMimeTypeParamOrder.eml");
        assertDiscoveryTestcaseResultProperties(result, true, true, this.testCred1);
    }

    @Test(dependsOnMethods = { "testIsMultipartSignature" })
    public void testDecryptMailXPkcs7MimeValid() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_valid.eml");
        assertDiscoveryTestcaseResultProperties(result, true, true, this.testCred1);
    }

    @Test
    public void testDecryptMailXPkcsMimeNoMatchingKeys() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_noMatchingKeys.eml");
        assertDiscoveryTestcaseResultProperties(result, true, false, null);
        // noinspection ConstantConditions
        Assert.assertNotEquals(result.getMailInfo().getTo(), this.testToAddr);
    }

    @Test
    public void testDecryptMailInvalidMimeType() throws IOException, MessagingException {
        DiscoveryTestcaseResult result = decryptAndParseEmail("core/mail/testDecryptMail_invalidMimeType.eml");
        assertDiscoveryTestcaseResultProperties(result, false, false, null);
        MailInfo mailInfo = result.getMailInfo();
        // noinspection ConstantConditions
        Assert.assertNull(mailInfo.getFrom());
        Assert.assertNull(mailInfo.getTo());
        Assert.assertFalse(result.hasTestcase());
    }

    private DiscoveryTestcaseResult decryptAndParseEmail(String mailLoc) throws IOException, MessagingException {
        try (InputStream mailInStream = ToolResourceUtils.getInputStream(mailLoc)) {
            ToolMimeMessageHelper mimeMsgHelper = new ToolMimeMessageHelper(new MimeMessage(this.mailSession, mailInStream), this.mailEnc);

            return this.testcaseProcessor.process(mimeMsgHelper,
                CollectionUtils.find(this.testDiscoveryTestcases, new DiscoveryTestcaseMailAddressPredicate(mimeMsgHelper.getTo())));
        }
    }

    private void assertDiscoveryTestcaseResultProperties(DiscoveryTestcaseResult result, boolean hasEncryptedMsg, boolean successful,
        DiscoveryTestcaseCredential credFound) throws MessagingException {
        MailInfo mailInfo = result.getMailInfo();

        Assert.assertNotNull(result);
        Assert.assertNotNull(mailInfo);
        Assert.assertEquals(mailInfo.hasEncryptedMessageHelper(), hasEncryptedMsg);
        Assert.assertEquals(result.isSuccessful(), successful);
        Assert.assertEquals(result.getCredentialFound(), credFound);

        if (mailInfo.hasEncryptedMessageHelper()) {
            // noinspection ConstantConditions
            if (mailInfo.getTo().equals(this.testToAddr)) {
                Assert.assertTrue(result.hasTestcase());
            } else {
                Assert.assertFalse(result.hasTestcase());
            }
        }

        if (successful) {
            Assert.assertEquals(result.getCredentialFound(), result.getCredentialExpected());
            Assert.assertFalse(mailInfo.hasDecryptionErrorMessage());
        }
    }

    @Test
    public void testIsMultipartSignature() {
        Assert.assertEquals(ToolMimeTypeUtils.forBaseType(this.testMultipartSignedMimeType),
            ToolMimeTypeUtils.forBaseType(MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG));
        Assert.assertEquals(ToolMimeTypeUtils.compareTo(true, this.testMultipartSignedMimeType, MailContentTypes.MULTIPART_SIGNED_PROTOCOL_PKCS7_SIG), 0);
        Assert.assertTrue(ToolMimeTypeUtils.hasParameter(this.testMultipartSignedMimeType, MailContentTypes.MULTIPART_SIGNED_MSG_DIGEST_ALG_PARAM_NAME));
        Assert.assertTrue(ToolMailContentTypeUtils.isMultipartSigned(this.testMultipartSignedMimeType));
    }

    @Test
    public void testMimeTypeParamOrder() {
        Assert.assertEquals(ToolMimeTypeUtils.compareTo(true, this.testPkcs7MimeContentType1, this.testPkcs7MimeContentType2), 0);
    }
}
