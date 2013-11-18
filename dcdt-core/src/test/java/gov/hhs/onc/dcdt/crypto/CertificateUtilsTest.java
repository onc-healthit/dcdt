package gov.hhs.onc.dcdt.crypto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.Security;
import java.security.cert.CertificateEncodingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.jce.provider.BouncyCastleProvider;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateUtilsTest {
    private CertificateInfo certificateInfo;
    private X509Certificate cert;
    private KeyPair keyPair;

    @BeforeClass
    public void setUp() throws CryptographyException {
        Security.addProvider(new BouncyCastleProvider());

        certificateInfo = mock(CertificateInfoImpl.class);
        CertificateName subject = mock(CertificateName.class);
        CertificateName issuer = mock(CertificateName.class);

        keyPair = KeyPairUtils.generateKeyPair(1024);
        PublicKey publicKey = keyPair.getPublic();
        SubjectPublicKeyInfo subjPubKeyInfo = new SubjectPublicKeyInfo(ASN1Sequence.getInstance(publicKey.getEncoded()));
        when(certificateInfo.getPublicKey()).thenReturn(subjPubKeyInfo);
        when(certificateInfo.getAuthKeyId()).thenReturn(new AuthorityKeyIdentifier(subjPubKeyInfo));
        when(certificateInfo.getSubjKeyId()).thenReturn(new SubjectKeyIdentifier(new byte[20]));

        when(certificateInfo.getIssuer()).thenReturn(issuer);
        when(certificateInfo.getIssuerPrivateKey()).thenReturn(keyPair.getPrivate());
        when(certificateInfo.getSubject()).thenReturn(subject);
        when(certificateInfo.getIssuer().toX500Name()).thenReturn(new X500Name("CN=issuer,C=country,ST=state,L=locality,O=organization,OU=organizationUnit"));
        when(certificateInfo.getSubject().toX500Name()).thenReturn(new X500Name("CN=subject,C=country,ST=state,L=locality,O=organization,OU=organizationUnit"));
        when(certificateInfo.getSerialNumber()).thenReturn(new BigInteger(String.valueOf(8)));
        when(certificateInfo.getSigAlgName()).thenReturn("SHA1WithRSAEncryption");

        CertificateValidInterval validInterval = mock(CertificateValidInterval.class);
        Date notBefore = new Date();
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(notBefore);
        calendar.add(Calendar.DATE, 100000);
        Date notAfter = calendar.getTime();
        when(validInterval.getNotBefore()).thenReturn(notBefore);
        when(validInterval.getNotAfter()).thenReturn(notAfter);
        when(certificateInfo.getValidInterval()).thenReturn(validInterval);

        when(certificateInfo.getDataEncoding()).thenReturn(DataEncoding.DER);
    }

    @Test
    public void testGenerateCertificate() throws CryptographyException {
        cert = CertificateUtils.generateCertificate(certificateInfo);

        try {
            Assert.assertNotNull(cert.getTBSCertificate());
        } catch (CertificateEncodingException e) {
            Assert.fail("Could not get DER-encoded certificate info", e);
        }
        Assert.assertTrue(cert.getSigAlgName().contains("SHA1WITHRSA"));
        Assert.assertEquals(cert.getNotBefore().toString(), certificateInfo.getValidInterval().getNotBefore().toString());
        Assert.assertEquals(cert.getNotAfter().toString(), certificateInfo.getValidInterval().getNotAfter().toString());
        Assert.assertEquals(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(cert.getPublicKey().getEncoded())), certificateInfo.getPublicKey());
        Assert.assertEquals(cert.getSerialNumber(), certificateInfo.getSerialNumber());
        Assert.assertEquals(cert.getIssuerDN().getName(), "CN=issuer,C=country,ST=state,L=locality,O=organization,OU=organizationUnit");
        Assert.assertEquals(cert.getSubjectDN().getName(), "CN=subject,C=country,ST=state,L=locality,O=organization,OU=organizationUnit");
    }

    @Test
    public void testGenerateSerialNum() throws CryptographyException {
        Set<BigInteger> existingSerialNums = new HashSet<>();
        BigInteger serialNum = CertificateUtils.generateSerialNum(existingSerialNums);
        Assert.assertTrue(existingSerialNums.contains(serialNum));
    }

    @Test(dataProvider = "encoding", dependsOnMethods = "testGenerateCertificate")
    public void testWriteReadCertificate(String fileName, String encoding) throws CryptographyException {
        try {
            File tempFile = File.createTempFile(fileName, "." + encoding);
            CertificateUtils.writeCertificate(tempFile, cert, encoding);
            X509Certificate certRead = CertificateUtils.readCertificate(tempFile, encoding);
            Assert.assertTrue(tempFile.delete());
            testCertificateAssertions(certRead);
        } catch (IOException e) {
            Assert.fail("Could not write " + encoding.toUpperCase() + "-encoded certificateInfo to temporary file.", e);
        }
    }

    @DataProvider(name = "encoding")
    private Object[][] parameterEncodingTestProvider() {
        return new Object[][] { { "testCert", DataEncoding.DER }, { "testCert", DataEncoding.PEM } };
    }

    private void testCertificateAssertions(X509Certificate certRead) {
        Assert.assertEquals(certRead.getNotBefore().toString(), cert.getNotBefore().toString());
        Assert.assertEquals(certRead.getNotAfter().toString(), cert.getNotAfter().toString());
        Assert.assertEquals(certRead.getPublicKey().toString(), cert.getPublicKey().toString());
        Assert.assertEquals(certRead.getSerialNumber(), cert.getSerialNumber());
        Assert.assertEquals(certRead.getIssuerDN().getName(), cert.getIssuerDN().getName());
        Assert.assertEquals(certRead.getSubjectDN().getName(), cert.getSubjectDN().getName());
    }
}
