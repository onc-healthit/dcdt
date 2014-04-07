package gov.hhs.onc.dcdt.beans;

import javax.annotation.Nullable;
import org.xbill.DNS.Name;

public interface ToolDomainBean extends ToolNamedBean {
    public boolean hasDomainName();

    @Nullable
    public Name getDomainName();

    public void setDomainName(@Nullable Name domainName);
}
