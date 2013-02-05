package gov.hhs.onc.dcdt.crypto;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.crypto.key" }, groups = { "dcdt.all", "dcdt.crypto.all", "dcdt.crypto.certName" })
public class CertificateNameTest
{
	@Test
	public void testToX500Name()
	{
		List<RDN> rdns = Arrays.asList(
			CertificateName.createRdn(BCStyle.EmailAddress, "acct@host"), 
			CertificateName.createRdn(BCStyle.CN, "commonName"), 
			CertificateName.createRdn(BCStyle.C, "US"), 
			CertificateName.createRdn(BCStyle.ST, "DC"), 
			CertificateName.createRdn(BCStyle.L, "locality"), 
			CertificateName.createRdn(BCStyle.O, "organization"), 
			CertificateName.createRdn(BCStyle.OU, "organizationUnit"));
		
		Collections.shuffle(rdns);
		
		// TODO: add assertion
	}
}