package gov.hhs.onc.dcdt.utils.test;

import gov.hhs.onc.dcdt.utils.Utility;

public class MockTestUtility extends Utility
{
	private final static String UTIL_NAME = "testutil";
	
	private static MockTestUtility instance;
	
	@SuppressWarnings("unchecked")
	public MockTestUtility()
	{
		super(UTIL_NAME, null);
	}

	public static MockTestUtility getInstance()
	{
		return (instance = (instance != null) ? instance : new MockTestUtility());
	}
}