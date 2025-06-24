package gov.hhs.onc.dcdt.crypto.crl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateGenerator;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialConfig;
import gov.hhs.onc.dcdt.crypto.credentials.CredentialInfo;
import gov.hhs.onc.dcdt.crypto.credentials.impl.CredentialInfoImpl;
import gov.hhs.onc.dcdt.crypto.keys.KeyGenerator;
import gov.hhs.onc.dcdt.crypto.keys.KeyInfo;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import java.math.BigInteger;
import java.util.Date;
import javax.annotation.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.crypto.keys.all", "dcdt.test.unit.crypto.certs.all" }, groups = { "dcdt.test.unit.crypto.all",
    "dcdt.test.unit.crypto.crl.all", "dcdt.test.unit.crypto.crl.gen" })
public class CrlGeneratorUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private KeyGenerator keyGen;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CertificateGenerator certGen;

    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CrlGenerator crlGen;

    @Resource(name = "testCredConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CredentialConfig testCredConfigCa1;

    @Resource(name = "testCrlConfigCa1")
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private CrlConfig testCrlConfigCa1;

    @Test
    public void testGenerateCrl() throws Exception {
        KeyInfo testCa1KeyPairInfo = this.keyGen.generateKeys(this.testCredConfigCa1.getKeyDescriptor(),null);
        CredentialInfo testCredInfoCa1 =
            new CredentialInfoImpl(testCa1KeyPairInfo, this.certGen.generateCertificate(testCa1KeyPairInfo, this.testCredConfigCa1.getCertificateDescriptor()));
        Date revocationDate = new Date(((new Date().getTime() / 1000L) * 1000L));
        BigInteger testCrlEntrySerialNum = this.testCrlConfigCa1.getEntries().keySet().iterator().next();
        CrlEntryConfig testCrlEntryConfig = this.testCrlConfigCa1.getEntries().get(testCrlEntrySerialNum);
        testCrlEntryConfig.setRevocationDate(revocationDate);

        CrlInfo testCrlInfoCa1 = this.crlGen.generateCrl(testCa1KeyPairInfo.getPrivateKeyInfo(), testCa1KeyPairInfo.getAuthorityKeyId(), this.testCrlConfigCa1);

        // noinspection ConstantConditions
        Assert.assertEquals(testCrlInfoCa1.getEntries().size(), 1, "Number of CRL entries do not match.");

        CrlEntryInfo testCrlEntryInfo = testCrlInfoCa1.getEntries().get(testCrlEntrySerialNum);

        Assert.assertEquals(testCrlEntryInfo.getSerialNumber(), testCrlEntrySerialNum, "CRL entry serial numbers do not match.");
        Assert.assertEquals(testCrlEntryInfo.getRevocationReason(), testCrlEntryConfig.getRevocationReason(), "CRL entry revocation reasons do not match.");
        Assert.assertEquals(testCrlEntryInfo.getRevocationDate().getTime(), revocationDate.getTime(), "CRL entry revocation dates do not match.");
    }
}
