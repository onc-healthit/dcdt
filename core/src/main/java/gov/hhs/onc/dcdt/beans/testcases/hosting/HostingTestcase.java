package gov.hhs.onc.dcdt.beans.testcases.hosting;

import gov.hhs.onc.dcdt.annotations.ConfigBean;
import gov.hhs.onc.dcdt.beans.testcases.Testcase;

@ConfigBean("testcases/hostingTestcase")
public class HostingTestcase extends Testcase<HostingTestcaseComments, HostingTestcaseResult>
{
	private HostingTestcaseLocation location;
	private HostingTestcaseBinding binding;
	
	public boolean isDns()
	{
		return this.location == HostingTestcaseLocation.DNS;
	}
	
	public boolean isLdap()
	{
		return this.location == HostingTestcaseLocation.LDAP;
	}
	
	public boolean isAddressBound()
	{
		return (this.binding == HostingTestcaseBinding.ADDRESS) || 
			(this.binding == HostingTestcaseBinding.BOTH);
	}
	
	public boolean isDomainBound()
	{
		return (this.binding == HostingTestcaseBinding.DOMAIN) || 
			(this.binding == HostingTestcaseBinding.BOTH);
	}
	
	public String getLocationName()
	{
		return this.hasLocation() ? this.location.getName() : null;
	}
	
	public void setLocationName(String name)
	{
		this.location = HostingTestcaseLocation.fromName(name);
	}
	
	public String getBindingName()
	{
		return this.hasBinding() ? this.binding.getName() : null;
	}
	
	public void setBindingName(String name)
	{
		this.binding = HostingTestcaseBinding.fromName(name);
	}

	public boolean hasBinding()
	{
		return this.binding != null;
	}
	
	public HostingTestcaseBinding getBinding()
	{
		return this.binding;
	}

	public void setBinding(HostingTestcaseBinding binding)
	{
		this.binding = binding;
	}

	public boolean hasLocation()
	{
		return this.location != null;
	}
	
	public HostingTestcaseLocation getLocation()
	{
		return this.location;
	}

	public void setLocation(HostingTestcaseLocation location)
	{
		this.location = location;
	}
}