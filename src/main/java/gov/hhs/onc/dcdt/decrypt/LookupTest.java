/**
 * 
 */
package gov.hhs.onc.dcdt.decrypt;

import gov.hhs.onc.dcdt.startup.ConfigInfo;

import java.util.HashMap;

/**
 * Contains the info for a single cert lookup test case
 * @author joelamy
 *
 */
public class LookupTest {
	
	// Initialize the lookup HashMap with all the tests and info for each success/failure path
	// Stage one of two stage initialization because it depends on config files.
	private static HashMap<String, LookupTest> lookupTests = new HashMap<String, LookupTest>();

	// This address tells us what test case we are dealing with
	private final String destinationAddress;

	// Displayable name
	private final String testCaseName;
	
	// Could also include description, etc. This would allow us to avoid hard-coding that stuff in the GUI.
	
	// Info about the certs the trace each possible path through the test case
	private final LookupTestCertInfo goodCertInfo;
	private final LookupTestCertInfo[] badCertInfoList;

	public LookupTest(String destinationAddress, String testCaseName,
			LookupTestCertInfo goodCertInfo, LookupTestCertInfo[] badCertInfoList) {
		super();
		this.destinationAddress = destinationAddress;
		this.testCaseName = testCaseName;
		this.goodCertInfo = goodCertInfo;
		this.badCertInfoList = badCertInfoList;
	}
	
	// Initialize the lookup HashMap with all the tests and info for each success/failure path
	// Stage two of two stage initialization because it depends on config files.
    public static void fillMap() {
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_500_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_500_EMAIL"), "DTS 500",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_1"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_2"),
							"Your system chose an address-bound DNS certificate for a different Direct address. It MUST have chosen the certificate for the correct address."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"),
							"Your System chose a valid domain-bound DNS Certificate. It MUST have chosen the valid address-bound DNS certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Your System chose a valid domain-bound DNS certificate for the wrong domain. It MUST have chosen the valid address-bound DNS certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_5"),
							"Your System chose a valid address-bound LDAP certificate. It MUST have chosen the DNS address-bound certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Your System chose a valid domain-bound LDAP certificate. It MUST have chosen the DNS CERT address-bound certificate first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_501_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_501_EMAIL"), "DTS 501",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_7"),
							"Your System used an expired certificate address-bound DNS certificate. It MUST NOT have used the expired certificate and SHOULD have used the valid domain-bound DNS certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Your System chose a valid domain-bound DNS certificate for the wrong domain. It MUST have chosen the valid domain-bound DNS certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_37"),
							"Your System chose an address-bound certificate from the LDAP server. It MUST have chosen the DNS CERT domain-bound certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Your System chose a valid domain-bound LDAP certificate. It MUST have chosen the DNS CERT domain-bound certificate first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_502_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_502_EMAIL"), "DTS 502",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_11"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"),
							"Your System used a valid domain-bound DNS certificate instead of the valid address-bound DNS certificate. Your System MUST have chosen the address bound first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Your System chose a valid domain-bound DNS certificate for the wrong domain. It MUST have chosen the valid address-bound DNS certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Your System chose a valid domain-bound certificate stored in an LDAP instance. It MUST have chosen the address-bound certificate in the DNS first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_505_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_505_EMAIL"), "DTS 505",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_14"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_13"),
							"Your System used an expired address-bound DNS certificate. It MUST not use this certificate because it is expired. It should have used the address-bound LDAP certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_38"),
							"Your System chose an expired DNS domain-bound certificate. It MUST not use this certificate because it is expired. It should have used the address-bound LDAP certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_16"),
							"Your system chose a valid domain-bound certificate in an LDAP server, but it MUST have used the valid address-bound certificate in the LDAP server before using the valid domain-bound certificate.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_515_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_515_EMAIL"), "DTS 515",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_16"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_39"),
							"Your System used an expired address-bound DNS certificate. It MUST not use this certificate because it is expired. It SHOULD have used the valid domain-bound LDAP certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_38"),
								"Your System chose an expired domain-bound DNS certificate. It MUST not use this certificate because it is expired. It SHOULD have used the valid domain-bound certificate in the Testing Tool's LDAP server."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_41"),
								"Your System chose an expired address-bound certificate from the LDAP server. It MUST not use this certificate because it is expired. It SHOULD have used the valid domain-bound certificate in the Testing Tool's LDAP server."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_17"),
							"Your system chose an address-bound certificate that was stored in a lower priority SRV record LDAP server. It SHOULD have chosen the domain-bound certificate in the higher priority SRV record first, though this is only recommended and not required.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_506_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_506_EMAIL"), "DTS 506",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_18"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_16"),
							"Your System chose a valid domain-bound LDAP certificate instead of the valid address-bound certificate stored in the LDAP instance. It MUST have chosen the valid address-bound certificate before using the valid domain-bound certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_19"),
							"Your System chose a valid certificate from a lower priority SRV Record. It SHOULD have chosen the valid address-bound certificate in the higher priority SRV Record LDAP instance. This is recommended practice, but not required.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_507_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_507_EMAIL"), "DTS 507",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_20"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_517_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_517_EMAIL"), "DTS 517",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_21"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_511_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_511_EMAIL"), "DTS 511",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_520_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_520_EMAIL"), "DTS 520",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_24"),
							"Your System used an expired address-bound DNS certificate. It MUST NOT use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_25"),
								"Your System used an expired domain-bound DNS certificate. It MUST NOT use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_26"),
								"Your System used an expired address-bound LDAP certificate. It MUST NOT use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_27"),
								"Your System used an expired domain-bound LDAP certificate. It MUST NOT use this because it's expired.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_512_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_512_EMAIL"), "DTS 512",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_28"),
							"Your System used an expired address-bound DNS certificate. It MUST NOT use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_29"),
							"Your System used an expired domain-bound DNS certificate. It MUST NOT use this because it's expired.")
					}
			));
	}

	// Returns null if there is no test case for this destination address
	public static LookupTest findLookupTest(String destAddr) {
		if (lookupTests.containsKey(destAddr))
			return lookupTests.get(destAddr);
		else
			return null;
	}

	public String getDestinationAddress() {
		return destinationAddress;
	}

	public String getTestCaseName() {
		return testCaseName;
	}
	
	public LookupTestCertInfo getGoodCertInfo() {
		return goodCertInfo;
	}
	public LookupTestCertInfo[] getBadCertInfoList() {
		return badCertInfoList;
	}
}
