package gov.hhs.onc.dcdt.beans.testcases.discovery;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.testcases.TestcaseResultStatus;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

@ConfigBean("testcases/discoveryTestcase")
public class DiscoveryTestcase extends ToolBean
{
	private String name;
	private String mailProperty;
	private Map<TestcaseResultStatus, ArrayList<DiscoveryTestcaseResult>> results = new HashMap<>();
	
	public void setResultsPass(ArrayList<DiscoveryTestcaseResult> results)
	{
		this.setResults(TestcaseResultStatus.PASS, results);
	}
	
	public void addResultsPass(DiscoveryTestcaseResult ... results)
	{
		this.addResults(TestcaseResultStatus.PASS, results);
	}
	
	public boolean hasResultsPass()
	{
		return this.hasResults(TestcaseResultStatus.PASS);
	}
	
	public ArrayList<DiscoveryTestcaseResult> getResultsPass()
	{
		return this.getResults(TestcaseResultStatus.PASS);
	}
	
	public void setResultsFail(ArrayList<DiscoveryTestcaseResult> results)
	{
		this.setResults(TestcaseResultStatus.FAIL, results);
	}
	
	public void addResultsFail(DiscoveryTestcaseResult ... results)
	{
		this.addResults(TestcaseResultStatus.FAIL, results);
	}
	
	public boolean hasResultsFail()
	{
		return this.hasResults(TestcaseResultStatus.FAIL);
	}
	
	public ArrayList<DiscoveryTestcaseResult> getResultsFail()
	{
		return this.getResults(TestcaseResultStatus.FAIL);
	}
	
	public void setResultsOptional(ArrayList<DiscoveryTestcaseResult> results)
	{
		this.setResults(TestcaseResultStatus.OPTIONAL, results);
	}
	
	public void addResultsOptional(DiscoveryTestcaseResult ... results)
	{
		this.addResults(TestcaseResultStatus.OPTIONAL, results);
	}
	
	public boolean hasResultsOptional()
	{
		return this.hasResults(TestcaseResultStatus.OPTIONAL);
	}
	
	public ArrayList<DiscoveryTestcaseResult> getResultsOptional()
	{
		return this.getResults(TestcaseResultStatus.OPTIONAL);
	}
	
	public void setResults(TestcaseResultStatus status, ArrayList<DiscoveryTestcaseResult> results)
	{
		if (results != null)
		{
			this.addResults(status, results.toArray(new DiscoveryTestcaseResult[results.size()]));
		}
	}
	
	public void addResults(TestcaseResultStatus status, DiscoveryTestcaseResult ... results)
	{
		this.getResults(status, true).addAll(Arrays.asList(results));
	}
	
	public void clearResults(TestcaseResultStatus status)
	{
		if (this.hasResults(status))
		{
			this.getResults(status).clear();
		}
	}
	
	public boolean hasResults(TestcaseResultStatus status)
	{
		return this.hasResults() && this.results.containsKey(status);
	}
	
	public ArrayList<DiscoveryTestcaseResult> getResults(TestcaseResultStatus status)
	{
		return this.getResults(status, false);
	}
	
	public ArrayList<DiscoveryTestcaseResult> getResults(TestcaseResultStatus status, boolean create)
	{
		if (create && !this.hasResults(status))
		{
			this.getResults(create).put(status, new ArrayList<DiscoveryTestcaseResult>());
		}
		
		return this.getResults(create).get(status);
	}
	
	public Map<TestcaseResultStatus, ArrayList<DiscoveryTestcaseResult>> getResults(boolean create)
	{
		return (this.results = (this.hasResults() ? this.results : 
			new HashMap<TestcaseResultStatus, ArrayList<DiscoveryTestcaseResult>>()));
	}
	
	public boolean hasMailProperty()
	{
		return !StringUtils.isBlank(this.mailProperty);
	}
	
	public String getMailProperty()
	{
		return this.mailProperty;
	}

	public void setMailProperty(String mailProperty)
	{
		this.mailProperty = mailProperty;
	}

	public boolean hasName()
	{
		return !StringUtils.isBlank(this.name);
	}
	
	public String getName()
	{
		return this.name;
	}

	public void setName(String name)
	{
		this.name = name;
	}

	public boolean hasResults()
	{
		return !MapUtils.isEmpty(this.results);
	}
	
	public Map<TestcaseResultStatus, ArrayList<DiscoveryTestcaseResult>> getResults()
	{
		return this.results;
	}

	public void setResults(Map<TestcaseResultStatus, ArrayList<DiscoveryTestcaseResult>> results)
	{
		this.results = results;
	}
}