/**
 * 
 */
package gov.onc.decrypt;

import java.util.HashMap;

/**
 * Contains the info for a single cert lookup test case
 * @author joelamy
 *
 */
public class LookupTest {
	
	// Initialize the lookup HashMap with all the tests and info for each success/failure path
	private static final HashMap<String, LookupTest> lookupTests = new HashMap<String, LookupTest>();
	static{
//		lookupTests.put("joe.lamy@nitorgroup.com",
//				new LookupTest("joe.lamy@nitorgroup.com", "Joe test",
//					// Good certs
//					new LookupTestCertInfo("nitor", "Passed"),	// These will match one test email
//					// Bad certs
//					new LookupTestCertInfo[] {
//						new LookupTestCertInfo("nitor3",	// Just copies of the nitor certs
//							"Used the nitor2 cert"),
//						new LookupTestCertInfo("nitor2",	// These will match one test email
//								"Used the nitor2 cert")
//					}
//			));
		lookupTests.put("dts500@direct1.testteam.us",
			new LookupTest("dts500@direct1.testteam.us", "DTS 500",
				// Good certs
				new LookupTestCertInfo("dts500_valid_cert_record", "Passed"),
				// Bad certs
				new LookupTestCertInfo[] {
					new LookupTestCertInfo("othercert",
						"This is not the correct address. It is for othercert@direct1.testteam.us"),
					new LookupTestCertInfo("dts501_valid",
						"Should have chosen the address bound first"),
					new LookupTestCertInfo("direct9.testteam.us",
						"Wrong domain and should have chosen the address bound certificate first"),
					new LookupTestCertInfo("dts500_valid_ldap",
						"Should have chosen the address bound certificate first"),
					new LookupTestCertInfo("dts501_valid_ldap", 
						"Should have chosen the address bound certificate first")
				}
		));
		lookupTests.put("dts501@direct1.testteam.us",
				new LookupTest("dts501@direct1.testteam.us", "DTS 501",
					// Good certs
					new LookupTestCertInfo("dts501_valid", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts501_expired",
							"Expired Certificate"),
						new LookupTestCertInfo("direct9.testteam.us",
							"Wrong domain"),
						new LookupTestCertInfo("dts501_valid_ldap", 
							"Your system chose a domain-bound certificate stored in an LDAP instance. Your system should have chosen the DNS CERT domain-bound certificate first")
					}
			));
		lookupTests.put("dts502@direct1.testteam.us",
				new LookupTest("dts502@direct1.testteam.us", "DTS 502",
					// Good certs
					new LookupTestCertInfo("dts502", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts501_valid",
							"Your system chose a valid domain-bound certificate stored in a CERT resource record. Your system should have chosen the address-bound certificate in the CERT resource record first"),
						new LookupTestCertInfo("direct9.testteam.us",
							"Wrong domain and should have chosen the address bound certificate first"),
						new LookupTestCertInfo("dts501_valid_ldap", 
							"Your System chose a valid domain-bound certificate stored in an LDAP instance. It should have chosen the address-bound certificate in the DNS first")
					}
			));
		lookupTests.put("dts505@direct2.testteam.us",
				new LookupTest("dts505@direct2.testteam.us", "DTS 505",
					// Good certs
					new LookupTestCertInfo("dts505_mac", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts505_expired_cert_record",
								"Your system chose an expired certificate from a DNS CERT resource record. It should have chosen the address bound certificate in the LDAP server."),
						new LookupTestCertInfo("dts505_unbound",
							"This certificate is not bound correctly"),
						new LookupTestCertInfo("dts515_mac",
							"Your system chose a valid domain-bound certificate in an LDAP server, but it should have used the valid address-bound certificate in the LDAP server.")
					}
			));
		lookupTests.put("dts515@direct2.testteam.us",
				new LookupTest("dts515@direct2.testteam.us", "DTS 515",
					// Good certs
					new LookupTestCertInfo("dts515_mac", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts515_address_bound",
							"Your system chose an address-bound certificate that was stored in a lower priority SRV record LDAP server. It SHOULD have chosen the domain-bound certificate in the higher priority SRV record first, though this is only recommended and not required.")
					}
			));
		lookupTests.put("dts506@direct2.testteam.us",
				new LookupTest("dts506@direct2.testteam.us", "DTS 506",
					// Good certs
					new LookupTestCertInfo("dts506_ldap_1_mac", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts515_mac",
							"Your system chose a domain-bound certificate instead of the valid address-bound certificate stored in the LDAP instance first."),
						new LookupTestCertInfo("dts506_ldap_2",
								"Your system chose a valid certificate from a lower priority SRV Record. It SHOULD have chosen the valid address-bound certificate in the higher priority SRV Record LDAP instance. This is recommended practice, but not required.")
					}
			));
		lookupTests.put("dts507@direct3.testteam.us",
				new LookupTest("dts507@direct3.testteam.us", "DTS 507",
					// Good certs
					new LookupTestCertInfo("dts507", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put("dts517@direct3.testteam.us",
				new LookupTest("dts517@direct3.testteam.us", "DTS 517",
					// Good certs
					new LookupTestCertInfo("dts517", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put("dts519@direct3.testteam.us",
				new LookupTest("dts519@direct3.testteam.us", "DTS 519",
					// Good certs
					new LookupTestCertInfo("dts519_valid_ldap_2", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts519_expired_ldap1_new",
							"Your system chose an expired certificate. It should have chosen the valid certificate in the lower priority SRV record LDAP instance.")
					}
			));
		lookupTests.put("dts511@direct4.testteam.us",
				new LookupTest("dts511@direct4.testteam.us", "DTS 511",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {}
			));
		lookupTests.put("dts520@direct5.testteam.us",
				new LookupTest("dts520@direct5.testteam.us", "DTS 520",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts520_invalid_address_cert",
							"Shouldn't use this because it's expired"),
						new LookupTestCertInfo("dts520_invalid_domain_cert",
								"Shouldn't use this because it's expired"),
						new LookupTestCertInfo("dts520_invalid_address_ldap",
								"Shouldn't use this because it's expired"),
						new LookupTestCertInfo("dts520_invalid_domain_ldap",
								"Shouldn't use this because it's expired")
					}
			));
		lookupTests.put("dts512@direct6.testteam.us",
				new LookupTest("dts512@direct6.testteam.us", "DTS 512",
					// Good certs
					null,
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts512_expired_address_cert",
							"Shouldn't use this because it's expired"),
						new LookupTestCertInfo("expired_direct6_domain_cert",
							"Shouldn't use this because it's expired")
					}
			));
		lookupTests.put("dts522@direct7.testteam.us",
				new LookupTest("dts522@direct7.testteam.us", "DTS 522",
					// Good certs
					new LookupTestCertInfo("direct7_valid", "Passed"),
					// Bad certs
					new LookupTestCertInfo[] {
						new LookupTestCertInfo("dts522_expired_address",
							"Shouldn't use this because it's expired")
					}
			));
	}

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
