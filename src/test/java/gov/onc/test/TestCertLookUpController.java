package gov.onc.test;

import static org.junit.Assert.*;
import gov.onc.certLookup.CertLookUpController;
import gov.onc.certLookup.CertificateInfo;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;


/**
 * @author joelamy
 * This class performs functional testing using the real external (DNS/LDAP) interfaces,
 * minus the GUI. It requires the DNS and LDAP servers that are used for the messaging tests.
 * TODO Consider renaming TestCertLookUpFunctionalWithRealExternalInterfaces
 */
public class TestCertLookUpController {

	@BeforeClass
	public static void setUpBeforeClass() throws Exception {
	}

	@AfterClass
	public static void tearDownAfterClass() throws Exception {
	}

	@Before
	public void setUp() throws Exception {
	}

	@After
	public void tearDown() throws Exception {
	}

	/**
	 * @param certificateInfo
	 * Helper method.
	 * This is based on analyzing the code - the common denominator for success/failure is
	 * Success: getResult will start with "Success"; getCertOutput will be variable
	 * Failure: getResult will be variable; getCertOutput will be empty or start with "Problem"
	 */
	private void runAndExpectSuccess(CertificateInfo certificateInfo) {
		CertLookUpController cluController = new CertLookUpController(certificateInfo);
		cluController.run();
		assertTrue("Result must start with \"Success\"", certificateInfo.getResult().startsWith("Success"));
		assertFalse("CertOutput must not start with \"Problem\"", certificateInfo.getCertOutput().startsWith("Problem"));
		assertFalse("CertOutput must not be empty", certificateInfo.getCertOutput().matches(""));
	}

	/**
	 * @param certificateInfo
	 * Helper method.
	 * This is based on analyzing the code - the common denominator for success/failure is
	 * Success: getResult will start with "Success"; getCertOutput will be variable
	 * Failure: getResult will be variable; getCertOutput will be empty or start with "Problem"
	 */
	private void runAndExpectFailure(CertificateInfo certificateInfo) {
		CertLookUpController cluController = new CertLookUpController(certificateInfo);
		cluController.run();
		assertFalse("Result must not start with \"Success\"", certificateInfo.getResult().startsWith("Success"));
		assertTrue("CertOutput must start with \"Problem\" or be empty",
				(certificateInfo.getCertOutput().matches("") ||
				certificateInfo.getCertOutput().startsWith("Problem")));
	}

	// Option 1: DTS 550 - DNS Address-bound Certificate Search
	
	/**
	 * Success
	 */
	@Test
	public void testDTS550_1() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500@direct1.testteam.us");
		runAndExpectSuccess(certificateInfo);
	}

	/**
	 * Failure - address not found at server
	 */
	@Test
	public void testDTS550_2() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts505@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}

	/**
	 * Failure - address already in domain format
	 */
	//@Test
	//public void testDTS550_3() {
		//CertificateInfo certificateInfo = new CertificateInfo(1, "dts500.direct1.testteam.us");
		//runAndExpectFailure(certificateInfo);
	//}

	/**
	 * Failure - invalid formats
	 */
	@Test
	public void testDTS550_4() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_5() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500@@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_6() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500..direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_7() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500.@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_8() {
		CertificateInfo certificateInfo = new CertificateInfo(1, ".dts500.direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_9() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500.direct1.testteam.us.");
		runAndExpectFailure(certificateInfo);
	}
	@Test
	public void testDTS550_10() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "ab(c)d,e:f;g<h>i[jk]l@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}

	/**
	 * Failure - domain not found at server
	 */
	@Test
	public void testDTS550_11() {
		CertificateInfo certificateInfo = new CertificateInfo(1, "dts500@direct.testteam.us");
		runAndExpectFailure(certificateInfo);
	}

	// Option 2: DTS 551 - DNS Domain-bound Certificate Search
	
	@Test
	public void testDTS551_1() {
		CertificateInfo certificateInfo = new CertificateInfo(2, "dts500@direct1.testteam.us");
		runAndExpectSuccess(certificateInfo);
	}

	// Option 3: DTS 573 - DNS Address-bound Certificate Search - Case Mismatch

	@Test
	public void testDTS573_1() {
		CertificateInfo certificateInfo = new CertificateInfo(3, "DTS500@DIRECT1.TESTTEAM.US");
		runAndExpectSuccess(certificateInfo);
	}

	// Option 4: DTS 556 - LDAP Address-bound Certificate Search

	/**
	 * RI - does not work in the web tool
	 */
	@Test
	public void testDTS556_1() {
		CertificateInfo certificateInfo = new CertificateInfo(4, "dts500@direct1.testteam.us");
		runAndExpectSuccess(certificateInfo);
	}

	/**
	 * TI - works in the web tool
	 */
	@Test
	public void testDTS556_2() {
		CertificateInfo certificateInfo = new CertificateInfo(4, "dts556@onctest.org");
		runAndExpectSuccess(certificateInfo);
	}

	// Option 5: DTS 557 - LDAP Address-bound Certificate Search - Case Mismatch

	/**
	 * RI - does not work in the web tool
	 */
	@Test
	public void testDTS557_1() {
		CertificateInfo certificateInfo = new CertificateInfo(5, "DTS500@DIRECT1.TESTTEAM.US");
		runAndExpectSuccess(certificateInfo);
	}

	/**
	 * TI - works in the web tool
	 */
	@Test
	public void testDTS557_2() {
		CertificateInfo certificateInfo = new CertificateInfo(5, "DTS556@ONCTEST.ORG");
		runAndExpectSuccess(certificateInfo);
	}

	// Option 6: DTS 570 - LDAP Domain-bound Certificate Search

	/**
	 * RI - does not work in the web tool
	 */
	@Test
	public void testDTS570_1() {
		CertificateInfo certificateInfo = new CertificateInfo(6, "dts500@direct1.testteam.us");
		runAndExpectSuccess(certificateInfo);
	}

	/**
	 * TI - works in the web tool
	 */
	@Test
	public void testDTS570_2() {
		CertificateInfo certificateInfo = new CertificateInfo(6, "whatever@onctest.org");
		runAndExpectSuccess(certificateInfo);
	}

	// Option 7: DTS 577 - LDAP - No Results Test

	@Test
	public void testDTS577_1() {
		CertificateInfo certificateInfo = new CertificateInfo(7, "dts502@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}

	/**
	 * Undefined test case
	 */
	@Test
	public void testUnrecognizedTestcase() {
		CertificateInfo certificateInfo = new CertificateInfo(8, "dts500@direct1.testteam.us");
		runAndExpectFailure(certificateInfo);
	}

}
