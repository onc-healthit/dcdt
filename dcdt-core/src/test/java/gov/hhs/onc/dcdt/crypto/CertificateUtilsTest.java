package gov.hhs.onc.dcdt.crypto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.File;
import java.io.IOException;
import java.math.BigInteger;
import java.security.KeyPair;
import java.security.PublicKey;
import java.security.cert.CertificateEncodingException;
import java.security.cert.CertificateParsingException;
import java.security.cert.X509Certificate;
import java.util.Calendar;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.asn1.x509.X509Extension;
import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.constants.DataEncoding;
import gov.hhs.onc.dcdt.crypto.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.CertificateUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateUtilsTest {
    private CertificateInfo certificateInfo;
    private X509Certificate leafCert;
    private CredentialInfo caCredentialInfo;
    private CredentialInfo leafCredentialInfo;
    private KeyPair caKeyPair;
    private final static String CA_X500_NAME = "CN=CA,C=country,ST=state,L=locality,O=organization,OU=organizationUnit";

    private void setupCACertificate() throws CryptographyException {
        certificateInfo = mock(CertificateInfoImpl.class);
        CertificateName subject = mock(CertificateName.class);

        caKeyPair = KeyPairUtils.generateKeyPair(1024);
        caCredentialInfo = mock(CredentialInfo.class);
        when(caCredentialInfo.getCertificateInfo()).thenReturn(certificateInfo);
        KeyPairInfo keyPairInfo = mock(KeyPairInfo.class);
        when(caCredentialInfo.getKeyPairInfo()).thenReturn(keyPairInfo);
        when(keyPairInfo.getPrivateKey()).thenReturn(caKeyPair.getPrivate());

        PublicKey publicKey = caKeyPair.getPublic();
        SubjectPublicKeyInfo subjPubKeyInfo = new SubjectPublicKeyInfo(ASN1Sequence.getInstance(publicKey.getEncoded()));
        when(certificateInfo.getPublicKey()).thenReturn(subjPubKeyInfo);

        when(certificateInfo.getSubject()).thenReturn(subject);
        when(certificateInfo.getIssuer()).thenReturn(subject);

        when(certificateInfo.getIssuer().toX500Name()).thenReturn(new X500Name(CA_X500_NAME));
        when(certificateInfo.getSubject().toX500Name()).thenReturn(new X500Name(CA_X500_NAME));
        when(certificateInfo.getSerialNumber()).thenReturn(new BigInteger(String.valueOf(8)));
        when(certificateInfo.getSigAlgName()).thenReturn("SHA1WithRSAEncryption");

        when(certificateInfo.getIsCa()).thenReturn(true);
        when(subject.getSubjAltNames()).thenReturn(new GeneralNames(new GeneralName(GeneralName.rfc822Name, "CA@testdomain.com")));

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

    private void setupLeafCertificate() throws CryptographyException {
        CertificateName subject = mock(CertificateName.class);
        CertificateName issuer = mock(CertificateName.class);

        KeyPair subjectKeyPair = KeyPairUtils.generateKeyPair(1024);

        leafCredentialInfo = mock(CredentialInfo.class);
        when(leafCredentialInfo.getCertificateInfo()).thenReturn(certificateInfo);

        PublicKey publicKey = subjectKeyPair.getPublic();
        SubjectPublicKeyInfo subjPubKeyInfo = new SubjectPublicKeyInfo(ASN1Sequence.getInstance(publicKey.getEncoded()));
        when(certificateInfo.getPublicKey()).thenReturn(subjPubKeyInfo);
        when(certificateInfo.getAuthKeyId()).thenReturn(new AuthorityKeyIdentifier(subjPubKeyInfo));
        when(certificateInfo.getSubjKeyId()).thenReturn(new SubjectKeyIdentifier(new byte[20]));

        when(certificateInfo.getIssuer()).thenReturn(issuer);
        when(certificateInfo.getIssuerPrivateKey()).thenReturn(caKeyPair.getPrivate());
        when(certificateInfo.getSubject()).thenReturn(subject);
        when(certificateInfo.getIssuer().toX500Name()).thenReturn(new X500Name("CN=issuer,C=country,ST=state,L=locality,O=organization,OU=organizationUnit"));
        when(certificateInfo.getSubject().toX500Name()).thenReturn(new X500Name("CN=subject,C=country,ST=state,L=locality,O=organization,OU=organizationUnit"));

        when(certificateInfo.getIsCa()).thenReturn(false);
        when(subject.getSubjAltNames()).thenReturn(new GeneralNames(new GeneralName(GeneralName.rfc822Name, "subject@testdomain.com")));
        when(issuer.getSubjAltNames()).thenReturn(new GeneralNames(new GeneralName(GeneralName.rfc822Name, "issuer@testdomain.com")));
    }

    @Test
    public void testGenerateCACertificate() throws CryptographyException, CertificateParsingException {
        setupCACertificate();

        X509Certificate caCert = CertificateUtils.generateCertificate(caCredentialInfo);

        try {
            Assert.assertNotNull(caCert.getTBSCertificate());
        } catch (CertificateEncodingException e) {
            Assert.fail("Could not get DER-encoded certificate info", e);
        }
        Assert.assertTrue(caCert.getSigAlgName().contains("SHA1WITHRSA"));
        Assert.assertEquals(caCert.getNotBefore().toString(), certificateInfo.getValidInterval().getNotBefore().toString());
        Assert.assertEquals(caCert.getNotAfter().toString(), certificateInfo.getValidInterval().getNotAfter().toString());
        Assert.assertEquals(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(caCert.getPublicKey().getEncoded())), certificateInfo.getPublicKey());
        Assert.assertEquals(caCert.getSerialNumber(), certificateInfo.getSerialNumber());
        Assert.assertEquals(caCert.getIssuerDN().getName(), CA_X500_NAME);
        Assert.assertEquals(caCert.getSubjectDN().getName(), CA_X500_NAME);

        Set<String> nonCriticalExtensions = caCert.getNonCriticalExtensionOIDs();
        Assert.assertTrue(nonCriticalExtensions.contains(X509Extension.basicConstraints.toString()));
        Assert.assertNotEquals(caCert.getBasicConstraints(), -1);
        Assert.assertFalse(nonCriticalExtensions.contains(X509Extension.authorityKeyIdentifier.toString()));
        Assert.assertFalse(nonCriticalExtensions.contains(X509Extension.subjectKeyIdentifier.toString()));

        Assert.assertNull(caCert.getIssuerAlternativeNames());
        Assert.assertNotNull(caCert.getSubjectAlternativeNames());
    }

    @Test(dependsOnMethods = "testGenerateCACertificate")
    public void testGenerateLeafCertificate() throws CryptographyException, CertificateParsingException {
        setupLeafCertificate();

        leafCert = CertificateUtils.generateCertificate(leafCredentialInfo);

        try {
            Assert.assertNotNull(leafCert.getTBSCertificate());
        } catch (CertificateEncodingException e) {
            Assert.fail("Could not get DER-encoded certificate info", e);
        }
        Assert.assertTrue(leafCert.getSigAlgName().contains("SHA1WITHRSA"));
        Assert.assertEquals(leafCert.getNotBefore().toString(), certificateInfo.getValidInterval().getNotBefore().toString());
        Assert.assertEquals(leafCert.getNotAfter().toString(), certificateInfo.getValidInterval().getNotAfter().toString());
        Assert.assertEquals(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(leafCert.getPublicKey().getEncoded())), certificateInfo.getPublicKey());
        Assert.assertEquals(leafCert.getSerialNumber(), certificateInfo.getSerialNumber());
        Assert.assertEquals(leafCert.getIssuerDN().getName(), "CN=issuer,C=country,ST=state,L=locality,O=organization,OU=organizationUnit");
        Assert.assertEquals(leafCert.getSubjectDN().getName(), "CN=subject,C=country,ST=state,L=locality,O=organization,OU=organizationUnit");

        Set<String> nonCriticalExtensions = leafCert.getNonCriticalExtensionOIDs();
        Assert.assertTrue(nonCriticalExtensions.contains(X509Extension.basicConstraints.toString()));
        Assert.assertEquals(leafCert.getBasicConstraints(), -1);
        Assert.assertTrue(nonCriticalExtensions.contains(X509Extension.authorityKeyIdentifier.toString()));
        Assert.assertTrue(nonCriticalExtensions.contains(X509Extension.subjectKeyIdentifier.toString()));

        Assert.assertNotNull(leafCert.getIssuerAlternativeNames());
        Assert.assertNotNull(leafCert.getSubjectAlternativeNames());
    }

    @Test
    public void testGenerateSerialNum() throws CryptographyException {
        Set<BigInteger> existingSerialNums = new HashSet<>();
        BigInteger serialNum = CertificateUtils.generateSerialNum(existingSerialNums);
        Assert.assertTrue(existingSerialNums.contains(serialNum));
    }

    @Test(dataProvider = "encoding", dependsOnMethods = "testGenerateLeafCertificate")
    public void testWriteReadCertificate(String fileName, String encoding) throws CryptographyException {
        try {
            File tempFile = File.createTempFile(fileName, "." + encoding);
            CertificateUtils.writeCertificate(tempFile, leafCert, encoding);
            X509Certificate certRead = CertificateUtils.readCertificate(tempFile, encoding);
            Assert.assertTrue(tempFile.delete());

            Assert.assertEquals(certRead.getNotBefore().toString(), leafCert.getNotBefore().toString());
            Assert.assertEquals(certRead.getNotAfter().toString(), leafCert.getNotAfter().toString());
            Assert.assertEquals(certRead.getPublicKey().toString(), leafCert.getPublicKey().toString());
            Assert.assertEquals(certRead.getSerialNumber(), leafCert.getSerialNumber());
            Assert.assertEquals(certRead.getIssuerDN().getName(), leafCert.getIssuerDN().getName());
            Assert.assertEquals(certRead.getSubjectDN().getName(), leafCert.getSubjectDN().getName());
        } catch (IOException e) {
            Assert.fail("Could not write " + encoding.toUpperCase() + "-encoded certificate to temporary file.", e);
        }
    }

    @DataProvider(name = "encoding")
    private Object[][] parameterEncodingTestProvider() {
        return new Object[][] { { "testCert", DataEncoding.DER }, { "testCert", DataEncoding.PEM } };
    }
}
