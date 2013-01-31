package gov.hhs.onc.dcdt.beans.testcases;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import org.apache.commons.collections.MapUtils;
import org.apache.commons.lang3.StringUtils;

public abstract class Testcase<T extends TestcaseComments, U extends TestcaseResult> extends ToolBean
{
	protected String name;
	protected T comments;
	protected Map<TestcaseResultStatus, ArrayList<U>> results = new HashMap<>();
	protected boolean negative;
	
	public void setResultsPass(ArrayList<U> results)
	{
		this.setResults(TestcaseResultStatus.PASS, results);
	}
	
	@SafeVarargs
	public final void addResultsPass(U ... results)
	{
		this.addResults(TestcaseResultStatus.PASS, results);
	}
	
	public boolean hasResultsPass()
	{
		return this.hasResults(TestcaseResultStatus.PASS);
	}
	
	public ArrayList<U> getResultsPass()
	{
		return this.getResults(TestcaseResultStatus.PASS);
	}
	
	public void setResultsFail(ArrayList<U> results)
	{
		this.setResults(TestcaseResultStatus.FAIL, results);
	}
	
	@SafeVarargs
	public final void addResultsFail(U ... results)
	{
		this.addResults(TestcaseResultStatus.FAIL, results);
	}
	
	public boolean hasResultsFail()
	{
		return this.hasResults(TestcaseResultStatus.FAIL);
	}
	
	public ArrayList<U> getResultsFail()
	{
		return this.getResults(TestcaseResultStatus.FAIL);
	}
	
	public void setResultsOptional(ArrayList<U> results)
	{
		this.setResults(TestcaseResultStatus.OPTIONAL, results);
	}
	
	@SafeVarargs
	public final void addResultsOptional(U ... results)
	{
		this.addResults(TestcaseResultStatus.OPTIONAL, results);
	}
	
	public boolean hasResultsOptional()
	{
		return this.hasResults(TestcaseResultStatus.OPTIONAL);
	}
	
	public ArrayList<U> getResultsOptional()
	{
		return this.getResults(TestcaseResultStatus.OPTIONAL);
	}
	
	public void setResults(TestcaseResultStatus status, ArrayList<U> results)
	{
		if (results != null)
		{
			this.addResults(status, results);
		}
	}
	
	@SafeVarargs
	public final void addResults(TestcaseResultStatus status, U ... results)
	{
		this.addResults(status, Arrays.asList(results));
	}
	
	public void addResults(TestcaseResultStatus status, Collection<U> results)
	{
		this.getResults(status, true).addAll(results);
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
	
	public ArrayList<U> getResults(TestcaseResultStatus status)
	{
		return this.getResults(status, false);
	}
	
	public ArrayList<U> getResults(TestcaseResultStatus status, boolean create)
	{
		if (create && !this.hasResults(status))
		{
			this.getResults(create).put(status, new ArrayList<U>());
		}
		
		return this.getResults(create).get(status);
	}
	
	public Map<TestcaseResultStatus, ArrayList<U>> getResults(boolean create)
	{
		return (this.results = (this.hasResults() ? this.results : 
			new HashMap<TestcaseResultStatus, ArrayList<U>>()));
	}

	public boolean hasComments()
	{
		return this.comments != null;
	}
	
	public T getComments()
	{
		return this.comments;
	}

	public void setComments(T comments)
	{
		this.comments = comments;
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

	public boolean isNegative()
	{
		return this.negative;
	}

	public void setNegative(boolean negative)
	{
		this.negative = negative;
	}

	public boolean hasResults()
	{
		return !MapUtils.isEmpty(this.results);
	}
	
	public Map<TestcaseResultStatus, ArrayList<U>> getResults()
	{
		return this.results;
	}

	public void setResults(Map<TestcaseResultStatus, ArrayList<U>> results)
	{
		this.results = results;
	}
}