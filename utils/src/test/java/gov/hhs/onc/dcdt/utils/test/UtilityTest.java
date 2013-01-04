package gov.hhs.onc.dcdt.utils.test;

import org.testng.annotations.BeforeClass;

public abstract class UtilityTest
{
	protected static MockTestUtility util;
	
	@BeforeClass
	public static void setUp()
	{
		util = MockTestUtility.getInstance();
	}
}