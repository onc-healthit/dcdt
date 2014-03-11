package gov.hhs.onc.dcdt.crypto.mail.decrypt;

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
import gov.hhs.onc.dcdt.mail.EmailInfo;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.test.impl.AbstractToolFunctionalTests;
import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcase;
import gov.hhs.onc.dcdt.testcases.discovery.credentials.DiscoveryTestcaseCredential;
import gov.hhs.onc.dcdt.testcases.discovery.results.DiscoveryTestcaseResultGenerator;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.Collection;
import java.util.List;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.func.crypto.all", "dcdt.test.func.crypto.mail.all", "dcdt.test.func.crypto.mail.decrypt" })
public class MailDecryptorFunctionalTests extends AbstractToolFunctionalTests {
    @Resource(name = "testDiscoveryTestcases")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private List<DiscoveryTestcase> discoveryTestcases;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private DiscoveryTestcaseResultGenerator resultGenerator;

    @Value("${dcdt.test.crypto.key.public.dts500}")
    private String testPublicKeyStr;

    @Value("${dcdt.test.crypto.key.private.dts500}")
    private String testPrivateKeyStr;

    @Value("${dcdt.test.crypto.cert.dts500}")
    private String testCertStr;

    @Value("${dcdt.test.lookup.domain.1.mail.addr.1}")
    private String testToAddr;

    @BeforeClass
    public void buildCredentialInfo() throws CryptographyException {
        KeyInfo testKeyInfo =
            new KeyInfoImpl(KeyUtils.readPublicKey(CryptographyUtils.getDerEncodedData(this.testPublicKeyStr), KeyAlgorithm.RSA, DataEncoding.DER),
                KeyUtils.readPrivateKey(CryptographyUtils.getDerEncodedData(this.testPrivateKeyStr), KeyAlgorithm.RSA, DataEncoding.DER));
        CertificateInfo testCertInfo =
            new CertificateInfoImpl(CertificateUtils.readCertificate(CryptographyUtils.getDerEncodedData(this.testCertStr), CertificateType.X509,
                DataEncoding.DER));
        CredentialInfo testCredInfo = new CredentialInfoImpl(testKeyInfo, testCertInfo);

        Collection<DiscoveryTestcaseCredential> targetCreds = this.discoveryTestcases.get(0).getTargetCredentials();
        targetCreds.iterator().next().setCredentialInfo(testCredInfo);
    }

    @Test
    public void testDecryptMailPkcs7MimeValid() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_valid.eml");
        assertEmailInfoProperties(emailInfo, true, true);
    }

    @Test
    public void testDecryptMailPkcs7MimeEncryptedWithWrongKey() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_encryptedWithWrongKey.eml");
        assertEmailInfoProperties(emailInfo, true, false);
    }

    @Test
    public void testDecryptMailPkcs7MimeDiffMimeTypeParamOrder() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_pkcs7-mime_diffMimeTypeParamOrder.eml");
        assertEmailInfoProperties(emailInfo, true, true);
    }

    @Test
    public void testDecryptMailXPkcs7MimeValid() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_valid.eml");
        assertEmailInfoProperties(emailInfo, true, true);
    }

    @Test
    public void testDecryptMailXPkcsMimeNoMatchingKeys() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_x-pkcs7-mime_noMatchingKeys.eml");
        assertEmailInfoProperties(emailInfo, true, false);
        Assert.assertFalse(emailInfo.hasDecryptedMessage());
        Assert.assertNotEquals(emailInfo.getToAddress(), this.testToAddr);
    }

    @Test
    public void testDecryptMailInvalidMimeType() {
        EmailInfo emailInfo = decryptAndParseEmail("core/mail/testDecryptMail_invalidMimeType.eml");
        assertEmailInfoProperties(emailInfo, false, false);
        Assert.assertNull(emailInfo.getFromAddress());
        Assert.assertNull(emailInfo.getToAddress());
        Assert.assertFalse(emailInfo.hasTestcase());
        Assert.assertFalse(emailInfo.hasDecryptedMessage());
    }

    private EmailInfo decryptAndParseEmail(String emailLoc) {
        try (InputStream emailInStream = ToolResourceUtils.getResourceInputStream(emailLoc)) {
            return this.resultGenerator.generateTestcaseResult(emailInStream, this.discoveryTestcases);
        } catch (IOException e) {
            return null;
        }
    }

    private void assertEmailInfoProperties(EmailInfo emailInfo, boolean hasEncryptedMsg, boolean successful) {
        Assert.assertNotNull(emailInfo);
        Assert.assertNotNull(emailInfo.getResultInfo());
        Assert.assertTrue(emailInfo.hasMessage());
        Assert.assertEquals(emailInfo.hasEncryptedMessage(), hasEncryptedMsg);
        Assert.assertEquals(emailInfo.getResultInfo().isSuccessful(), successful);

        if (emailInfo.hasEncryptedMessage()) {
            if (emailInfo.getToAddress().equals(this.testToAddr)) {
                Assert.assertTrue(emailInfo.hasTestcase());
            } else {
                Assert.assertFalse(emailInfo.hasTestcase());
            }
        }

        if (successful) {
            Assert.assertTrue(emailInfo.hasDecryptedMessage());
        }
    }
}
