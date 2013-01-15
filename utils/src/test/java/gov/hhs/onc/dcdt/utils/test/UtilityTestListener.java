package gov.hhs.onc.dcdt.utils.test;

import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class UtilityTestListener implements ITestListener
{
	private final static String METHOD_ARGS_PREFIX = "(";
	private final static String METHOD_ARGS_SUFFIX = ")";
	
	private final static Logger LOGGER = Logger.getLogger(UtilityTestListener.class);
	
	@Override
	public void onTestStart(ITestResult result)
	{
		LOGGER.trace("Started test: " + getTestCall(result, true));
	}

	@Override
	public void onTestSuccess(ITestResult result)
	{
		LOGGER.trace("Test succeeded: " + getTestCall(result, false));
	}

	@Override
	public void onTestFailure(ITestResult result)
	{
		LOGGER.trace("Test failed: " + getTestCall(result, false), result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result)
	{
		LOGGER.trace("Test skipped: " + getTestCall(result, false));
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result)
	{
	}

	@Override
	public void onStart(ITestContext context)
	{
	}
	
	@Override
	public void onFinish(ITestContext context)
	{
	}
	
	private static String getTestCall(ITestResult result, boolean includeParams)
	{
		StringBuilder builder = new StringBuilder();
		ITestNGMethod method = result.getMethod();
		
		builder.append(method.getTestClass().getName());
		builder.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
		builder.append(method.getMethodName());
		builder.append(METHOD_ARGS_PREFIX);
		
		if (includeParams)
		{
			Object[] params = result.getParameters();
			
			for (int a = 0; a < params.length; a++)
			{
				if (a > 0)
				{
					builder.append(", ");
				}
				
				builder.append(ClassUtils.getShortClassName(params[a], null));
			}
		}
		
		builder.append(METHOD_ARGS_SUFFIX);
		
		return builder.toString();
	}
}