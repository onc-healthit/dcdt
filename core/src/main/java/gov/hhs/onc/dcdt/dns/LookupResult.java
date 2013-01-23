package gov.hhs.onc.dcdt.dns;

import java.util.ArrayList;
import java.util.List;
import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.xbill.DNS.Lookup;
import org.xbill.DNS.Name;
import org.xbill.DNS.Record;

public class LookupResult<T extends Record>
{
	private Name question;
	private Lookup lookup;
	private Class<T> recordClass;
	
	public LookupResult(Name question, Lookup lookup, Class<T> recordClass)
	{
		this.question = question;
		this.lookup = lookup;
		this.recordClass = recordClass;
	}

	@Override
	public String toString()
	{
		StringBuilder builder = new StringBuilder();
		
		if (this.hasQuestion())
		{
			builder.append("question=");
			builder.append(this.question);
		}
		
		if (this.hasResultType())
		{
			if (builder.length() > 0)
			{
				builder.append(", ");
			}
			
			builder.append("resultType=");
			builder.append(this.getResultType());
			
			if (this.hasErrorString())
			{
				if (builder.length() > 0)
				{
					builder.append(", ");
				}
				
				builder.append("error=");
				builder.append(this.getErrorString());
			}
			
			if (this.hasRecords())
			{
				if (builder.length() > 0)
				{
					builder.append(", ");
				}
				
				builder.append("records=[");
				builder.append(StringUtils.join(this.getRecords(), ", "));
				builder.append("]");
			}
		}
		
		return builder.toString();
	}
	
	public boolean hasRecords()
	{
		return !CollectionUtils.isEmpty(this.getRecords());
	}
	
	public T getRecord()
	{
		List<T> records = this.getRecords();
		
		return !CollectionUtils.isEmpty(records) ? records.get(0) : null;
	}
	
	@SuppressWarnings("unchecked")
	public List<T> getRecords()
	{
		if (!this.isLookupComplete())
		{
			return null;
		}
		
		Record[] answers = this.lookup.getAnswers();
		List<T> records = new ArrayList<>(ArrayUtils.getLength(answers));
		
		if (answers != null)
		{
			for (Record answer : answers)
			{
				records.add((T)answer);
			}
		}
		
		return records;
	}

	public boolean hasErrorString()
	{
		return !StringUtils.isBlank(this.getErrorString());
	}
	
	public String getErrorString()
	{
		return this.isLookupComplete() ? this.lookup.getErrorString() : null;
	}
	
	public boolean isLookupComplete()
	{
		LookupResultType resultType = this.getResultType();
		
		return (resultType != null) && !resultType.isUnknown();
	}
	
	public boolean hasResultType()
	{
		return this.getResultType() != null;
	}
	
	public LookupResultType getResultType()
	{
		if (!this.hasLookup())
		{
			return null;
		}
		
		try
		{
			return this.hasLookup() ? LookupResultType.fromResult(this.lookup.getResult()) : null;
		}
		catch (IllegalStateException e)
		{
			return LookupResultType.UNKNOWN;
		}
	}
	
	public boolean hasLookup()
	{
		return this.lookup != null;
	}
	
	public Lookup getLookup()
	{
		return this.lookup;
	}

	public void setLookup(Lookup lookup)
	{
		this.lookup = lookup;
	}

	public boolean hasQuestion()
	{
		return this.question != null;
	}
	
	public Name getQuestion()
	{
		return this.question;
	}

	public void setQuestion(Name question)
	{
		this.question = question;
	}

	public boolean hasRecordClass()
	{
		return this.recordClass != null;
	}

	public Class<T> getRecordClass()
	{
		return this.recordClass;
	}
}