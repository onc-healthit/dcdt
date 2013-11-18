package gov.hhs.onc.dcdt.crypto;

import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x509.GeneralName;
import org.testng.Assert;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import gov.hhs.onc.dcdt.crypto.impl.CertificateNameImpl;

@Test(groups = { "dcdt.test.all", "dcdt.test.unit.all", "dcdt.test.unit.crypto.all", "dcdt.test.unit.crypto.cert" })
public class CertificateNameTest {
    private CertificateName subject;
    private CertificateName subject2;
    private CertificateName subject3;

    @BeforeClass
    public void setUp() {
        subject = new CertificateNameImpl();
        subject.setCommonName("commonName");
        subject.setCountry("country");
        subject.setState("state");
        subject.setLocality("locality");
        subject.setOrganization("organization");
        subject.setOrganizationUnit("organizationUnit");

        subject2 = new CertificateNameImpl();
        subject2.setMail("test@testdomain.com");
        subject2.setCommonName("commonName");
        subject2.setCountry("country");
        subject2.setState("state");
        subject2.setLocality("locality");
        subject2.setOrganization("organization");
        subject2.setOrganizationUnit("organizationUnit");

        subject3 = new CertificateNameImpl();
        subject3.setOrganization("organization");
        subject3.setState("state");
        subject3.setOrganizationUnit("organizationUnit");
        subject3.setCommonName("commonName");
        subject3.setCountry("country");
        subject3.setLocality("locality");
    }

    @Test
    public void testToX500Name() {
        Assert.assertNotNull(subject.toX500Name());
        Assert.assertNotNull(subject2.toX500Name());
    }

    @Test
    public void testEquals() {
        Assert.assertFalse(subject.equals(subject2));
        Assert.assertFalse(subject2.equals(subject3));
        Assert.assertTrue(subject.equals(subject3));
    }

    @Test
    public void testGetSubjAltNamesMail() {
        CertificateName certName = new CertificateNameImpl();
        String mail = "test@testdomain.com";
        certName.setMail(mail);
        GeneralName generalName = certName.getSubjAltNames().getNames()[0];
        Assert.assertEquals(generalName.getTagNo(), GeneralName.rfc822Name);
        Assert.assertEquals(generalName.getName().toString(), mail);
    }

    @Test
    public void testGetSubjAltNamesNotMail() {
        CertificateName certName = new CertificateNameImpl();
        String mail = "test.testdomain.com";
        certName.setMail(mail);
        GeneralName generalName = certName.getSubjAltNames().getNames()[0];
        Assert.assertEquals(generalName.getTagNo(), GeneralName.dNSName);
        Assert.assertEquals(generalName.getName().toString(), mail);
    }

    @Test
    public void testHasValue() {
        Assert.assertTrue(subject.hasValue(BCStyle.CN));
        Assert.assertTrue(subject.hasValue(BCStyle.C));
        Assert.assertTrue(subject.hasValue(BCStyle.ST));
        Assert.assertTrue(subject.hasValue(BCStyle.L));
        Assert.assertTrue(subject.hasValue(BCStyle.O));
        Assert.assertTrue(subject.hasValue(BCStyle.OU));
        Assert.assertFalse(subject.hasValue(BCStyle.EmailAddress));
        Assert.assertEquals(subject.getRdnMap().size(), 6);

        Assert.assertTrue(subject2.hasValue(BCStyle.CN));
        Assert.assertTrue(subject2.hasValue(BCStyle.C));
        Assert.assertTrue(subject2.hasValue(BCStyle.ST));
        Assert.assertTrue(subject2.hasValue(BCStyle.L));
        Assert.assertTrue(subject2.hasValue(BCStyle.O));
        Assert.assertTrue(subject2.hasValue(BCStyle.OU));
        Assert.assertTrue(subject2.hasValue(BCStyle.EmailAddress));
        Assert.assertEquals(subject2.getRdnMap().size(), 7);
    }
}
