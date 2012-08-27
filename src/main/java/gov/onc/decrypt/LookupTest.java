/**
 * 
 */
package gov.onc.decrypt;

import gov.onc.startup.ConfigInfo;

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
							"This cert is not for the correct address."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"),
							"Should have chosen the address bound first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Wrong domain and should have chosen the address bound certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_5"),
							"Should have chosen the address bound certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Should have chosen the address bound certificate first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_501_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_501_EMAIL"), "DTS 501",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_7"),
							"Expired Certificate."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Wrong domain."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Your system chose a domain-bound certificate stored in an LDAP instance. Your system should have chosen the DNS CERT domain-bound certificate first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_502_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_502_EMAIL"), "DTS 502",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_11"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_3"),
							"Your system chose a valid domain-bound certificate stored in a CERT resource record. Your system should have chosen the address-bound certificate in the CERT resource record first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_4"),
							"Wrong domain and should have chosen the address bound certificate first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_6"), 
							"Your System chose a valid domain-bound certificate stored in an LDAP instance. It should have chosen the address-bound certificate in the DNS first.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_505_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_505_EMAIL"), "DTS 505",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_14"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_13"),
								"Your system chose an expired certificate from a DNS CERT resource record. It should have chosen the address bound certificate in the LDAP server."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_15"),
							"This certificate is not bound correctly."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_16"),
							"Your system chose a valid domain-bound certificate in an LDAP server, but it should have used the valid address-bound certificate in the LDAP server.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_515_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_515_EMAIL"), "DTS 515",
					// Good certs
					new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_16"), "Passed."),
					// Bad certs
					new LookupTestCertInfo[] {
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
							"Your system chose a domain-bound certificate instead of the valid address-bound certificate stored in the LDAP instance first."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_19"),
								"Your system chose a valid certificate from a lower priority SRV Record. It SHOULD have chosen the valid address-bound certificate in the higher priority SRV Record LDAP instance. This is recommended practice, but not required.")
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
							"Shouldn't use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_25"),
								"Shouldn't use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_26"),
								"Shouldn't use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_27"),
								"Shouldn't use this because it's expired.")
					}
			));
		lookupTests.put(ConfigInfo.getConfigProperty("DTS_512_EMAIL"),
				new LookupTest(ConfigInfo.getConfigProperty("DTS_512_EMAIL"), "DTS 512",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_28"),
							"Shouldn't use this because it's expired."),
						new LookupTestCertInfo(ConfigInfo.getConfigProperty("CERT_29"),
							"Shouldn't use this because it's expired.")
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
