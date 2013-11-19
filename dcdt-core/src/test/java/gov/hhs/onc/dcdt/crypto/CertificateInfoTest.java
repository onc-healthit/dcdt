package gov.hhs.onc.dcdt.crypto;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.security.KeyPair;

import org.bouncycastle.asn1.ASN1Sequence;
import org.bouncycastle.asn1.x509.SubjectPublicKeyInfo;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.impl.CertificateInfoImpl;
import gov.hhs.onc.dcdt.crypto.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.impl.KeyPairInfoImpl;
import gov.hhs.onc.dcdt.crypto.utils.KeyPairUtils;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateInfoTest {
    private CertificateInfo certificateInfo;

    @BeforeClass
    public void setUp() throws CryptographyException {
        certificateInfo = new CertificateInfoImpl();
        CredentialInfo issuerCredentialInfo = mock(CredentialInfoImpl.class);
        certificateInfo.setIssuerCredentials(issuerCredentialInfo);
        KeyPairInfo issuerKeyPairInfo = mock(KeyPairInfoImpl.class);
        KeyPair issuerKeyPair = KeyPairUtils.generateKeyPair(1024);
        KeyPair subjectKeyPair = KeyPairUtils.generateKeyPair(1024);

        when(issuerCredentialInfo.getKeyPairInfo()).thenReturn(issuerKeyPairInfo);
        when(issuerCredentialInfo.getKeyPairInfo().getPublicKey()).thenReturn(issuerKeyPair.getPublic());
        certificateInfo.setPublicKey(new SubjectPublicKeyInfo(ASN1Sequence.getInstance(subjectKeyPair.getPublic().getEncoded())));
    }

    @Test
    public void testGetAuthKeyId() {
        Assert.assertNotNull(certificateInfo.getAuthKeyId());
    }

    @Test
    public void testGetSubjKeyId() {
        Assert.assertNotNull(certificateInfo.getSubjKeyId());
    }
}
