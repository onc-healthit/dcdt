package gov.hhs.onc.dcdt.utils.data.service;

import gov.hhs.onc.dcdt.utils.UtilityException;

public class DataServiceException extends UtilityException
{
	public DataServiceException()
	{
		super();
	}

	public DataServiceException(Throwable cause)
	{
		super(cause);
	}

	public DataServiceException(String message)
	{
		super(message);
	}

	public DataServiceException(String message, Throwable cause)
	{
		super(message, cause);
	}
}