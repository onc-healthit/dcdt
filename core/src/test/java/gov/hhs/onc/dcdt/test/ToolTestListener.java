package gov.hhs.onc.dcdt.test;

import org.apache.commons.lang3.ClassUtils;
import org.apache.log4j.Logger;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class ToolTestListener implements ITestListener
{
	private final static String METHOD_ARGS_PREFIX = "(";
	private final static String METHOD_ARGS_SUFFIX = ")";
	
	private final static Logger LOGGER = Logger.getLogger(ToolTestListener.class);
	
	@Override
	public void onTestStart(ITestResult result)
	{
		LOGGER.trace("Test started: " + testResultToString(result, true));
	}

	@Override
	public void onTestSuccess(ITestResult result)
	{
		LOGGER.trace("Test succeeded: " + testResultToString(result, false));
	}

	@Override
	public void onTestFailure(ITestResult result)
	{
		LOGGER.trace("Test failed: " + testResultToString(result, false), result.getThrowable());
	}

	@Override
	public void onTestSkipped(ITestResult result)
	{
		LOGGER.trace("Test skipped: " + testResultToString(result, false));
	}

	@Override
	public void onTestFailedButWithinSuccessPercentage(ITestResult result)
	{
	}

	@Override
	public void onStart(ITestContext context)
	{
		LOGGER.trace("Test context started: " + testContextToString(context));
	}
	
	@Override
	public void onFinish(ITestContext context)
	{
		LOGGER.trace("Test context finished: " + testContextToString(context));
	}
	
	private static String testContextToString(ITestContext testContext)
	{
		StringBuilder builder = new StringBuilder();
		builder.append("name=");
		builder.append(testContext.getName());
		
		// TODO: add remaining test context data
		
		return builder.toString();
	}
	
	private static String testResultToString(ITestResult testResult, boolean includeParams)
	{
		StringBuilder builder = new StringBuilder();
		ITestNGMethod testMethod = testResult.getMethod();
		
		builder.append(testMethod.getTestClass().getName());
		builder.append(ClassUtils.PACKAGE_SEPARATOR_CHAR);
		builder.append(testMethod.getMethodName());
		builder.append(METHOD_ARGS_PREFIX);
		
		if (includeParams)
		{
			Object[] params = testResult.getParameters();
			
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