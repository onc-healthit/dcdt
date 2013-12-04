package gov.hhs.onc.dcdt.crypto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.KeyPair;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.AuthorityKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectKeyIdentifier;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.bouncycastle.crypto.Digest;
import org.bouncycastle.crypto.digests.SHA1Digest;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.impl.KeyPairInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" }, dependsOnGroups = { "dcdt.test.unit.crypto.key" })
public class CertificateInfoTest {
    private CertificateInfo certificateInfo;
    private KeyPair issuerKeyPair;

    @BeforeClass
    public void setupCA() throws CryptographyException {
        certificateInfo = new CertificateInfoImpl();
        CredentialInfo issuerCredentialInfo = mock(CredentialInfoImpl.class);
        certificateInfo.setIssuerCredentials(issuerCredentialInfo);
        KeyPairInfo issuerKeyPairInfo = mock(KeyPairInfoImpl.class);
        issuerKeyPair = KeyPairUtils.generateKeyPair(1024);
        KeyPair subjectKeyPair = issuerKeyPair;

        when(issuerCredentialInfo.getKeyPairInfo()).thenReturn(issuerKeyPairInfo);
        when(issuerCredentialInfo.getKeyPairInfo().getPublicKey()).thenReturn(issuerKeyPair.getPublic());
        certificateInfo.setPublicKey(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.getPublic().getEncoded())));
    }

    @Test
    public void testGetAuthKeyId() {
        Assert.assertEquals(certificateInfo.getAuthKeyId(),
            new AuthorityKeyIdentifier(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(issuerKeyPair.getPublic().getEncoded()))));
    }

    @Test
    public void testGetSubjKeyId() {
        Digest subjKeyDigest = new SHA1Digest();
        byte[] subjKeyIdData = new byte[subjKeyDigest.getDigestSize()];
        byte[] pubKeyData = certificateInfo.getPublicKey().getPublicKeyData().getBytes();
        subjKeyDigest.update(pubKeyData, 0, pubKeyData.length);
        subjKeyDigest.doFinal(subjKeyIdData, 0);
        Assert.assertEquals(certificateInfo.getSubjKeyId(), new SubjectKeyIdentifier(subjKeyIdData));
    }

    @Test
    public void testGetCAAuthAndSubjKeyId() throws CryptographyException {
        certificateInfo.setIsCa(true);
        Assert.assertEquals(certificateInfo.getSubjKeyId().getKeyIdentifier(), certificateInfo.getAuthKeyId().getKeyIdentifier());
    }

    @Test(dependsOnMethods = "testGetCAAuthAndSubjKeyId")
    public void testGetLeafAuthAndSubjKeyId() throws CryptographyException {
        KeyPair subjectKeyPair = KeyPairUtils.generateKeyPair(1024);

        Assert.assertNotEquals(issuerKeyPair, subjectKeyPair);
        certificateInfo.setPublicKey(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.getPublic().getEncoded())));

        certificateInfo.setIsCa(false);
        Assert.assertNotEquals(certificateInfo.getSubjKeyId().getKeyIdentifier(), certificateInfo.getAuthKeyId().getKeyIdentifier());

        CertificateInfo issuerCertificateInfo = new CertificateInfoImpl();
        issuerCertificateInfo.setPublicKey(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(issuerKeyPair.getPublic().getEncoded())));
        Assert.assertEquals(certificateInfo.getAuthKeyId().getKeyIdentifier(), issuerCertificateInfo.getSubjKeyId().getKeyIdentifier());
    }

}
