package gov.onc.test;

import static org.junit.Assert.*;
import gov.onc.email.property.EmailPropertyHandler;

import org.junit.Test;

public class TestEmailPropertyHandler {

	@Test
	public void testStripDomain() {
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testAddr = "test@gmail.com";
		String testDomain = "gmail.com";
		
		try {
			assertEquals(eph.stripDomain(testAddr), testDomain);
		} catch (Exception e) {
			assertTrue(false);
		}
	}
	
	@Test(expected = Exception.class)
	public void testStripDomainNoArobaException() throws Exception{
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testAddr = "notAnEmail";
		
		eph.stripDomain(testAddr);
	}
	
	@Test(expected = Exception.class)
	public void testStripDomainNoDomainException() throws Exception{
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testAddr = "test@";
		
		eph.stripDomain(testAddr);
	}
	
	@Test
	public void testValidEmail() {
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testDomain = "gmail.com";
		assertTrue(eph.isEmailValid(testDomain));
	}
	
	@Test
	public void testInvalidDomain() {
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testDomain = "invalid_domain";

		assertFalse(eph.isEmailValid(testDomain));
			
	}
	@Test
	public void testNoDomain() {
		EmailPropertyHandler eph = new EmailPropertyHandler();
		String testDomain = "";
			assertFalse(eph.isEmailValid(testDomain));
	}
	
}
