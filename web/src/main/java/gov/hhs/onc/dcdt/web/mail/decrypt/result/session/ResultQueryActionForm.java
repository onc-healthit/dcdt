package gov.hhs.onc.dcdt.web.mail.decrypt.result.session;

import gov.hhs.onc.dcdt.web.mail.decrypt.EmailBean;
import java.util.List;
import org.apache.struts.action.ActionForm;

public class ResultQueryActionForm extends ActionForm
{
	private String directMailAddr;
	private List<EmailBean> results;

	public String getDirectMailAddr()
	{
		return this.directMailAddr;
	}

	public void setDirectMailAddr(String directMailAddr)
	{
		this.directMailAddr = directMailAddr;
	}

	public List<EmailBean> getResults()
	{
		return this.results;
	}

	public void setResults(List<EmailBean> results)
	{
		this.results = results;
	}
}