package gov.hhs.onc.dcdt.test;

import org.apache.commons.lang3.ClassUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.log4j.Logger;
import org.testng.ISuite;
import org.testng.ISuiteListener;
import org.testng.ITestContext;
import org.testng.ITestListener;
import org.testng.ITestNGMethod;
import org.testng.ITestResult;

public class ToolTestListener implements ISuiteListener, ITestListener
{
	private final static String METHOD_ARGS_PREFIX = "(";
	private final static String METHOD_ARGS_SUFFIX = ")";
	
	private final static Logger LOGGER = Logger.getLogger(ToolTestListener.class);

	//<editor-fold desc="Implemented ISuiteListener methods">
	@Override
	public void onStart(ISuite suite)
	{
		LOGGER.debug("Test suite started: " + testSuiteToString(suite));
	}
	
	@Override
	public void onFinish(ISuite suite)
	{
		LOGGER.debug("Test suite finished: " + testSuiteToString(suite));
	}
	//</editor-fold>

	//<editor-fold desc="Implemented ITestListener methods">
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
		LOGGER.debug("Test context started: " + testContextToString(context));
	}
	
	@Override
	public void onFinish(ITestContext context)
	{
		LOGGER.debug("Test context finished: " + testContextToString(context));
	}
	//</editor-fold>
	
	private static String testSuiteToString(ISuite testSuite)
	{
		return new StrBuilder()
			.append("name=")
			.append(testSuite.getName())
			.toString();
		
		// TODO: add remaining test suite data
	}
	
	private static String testContextToString(ITestContext testContext)
	{
		return new StrBuilder()
			.append("name=")
			.append(testContext.getName())
			.toString();
		
		// TODO: add remaining test context data
	}
	
	private static String testResultToString(ITestResult testResult, boolean includeParams)
	{
		StrBuilder builder = new StrBuilder();
		String testName = testResult.getTestName();
		
		if (!StringUtils.isBlank(testName))
		{
			builder = builder.append("name=")
				.append(testName);
		}
		
		ITestNGMethod testMethod = testResult.getMethod();
		
		builder = builder.appendSeparator(", ")
			.append(testMethod.getTestClass().getName())
			.append(ClassUtils.PACKAGE_SEPARATOR_CHAR)
			.append(testMethod.getMethodName())
			.append(METHOD_ARGS_PREFIX);
		
		if (includeParams)
		{
			StrBuilder paramsBuilder = new StrBuilder();
			
			for (Object param : testResult.getParameters())
			{
				paramsBuilder = paramsBuilder.appendSeparator(", ")
					.append(ClassUtils.getShortClassName(param, null));
			}
			
			builder = builder.append(paramsBuilder);
		}
		
		return builder.append(METHOD_ARGS_SUFFIX).toString();
	}
}