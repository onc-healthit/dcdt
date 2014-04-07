package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolDomainBean;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.xbill.DNS.Name;

@MappedSuperclass
public abstract class AbstractToolDomainBean extends AbstractToolNamedBean implements ToolDomainBean {
    protected Name domainName;

    @Override
    public boolean hasDomainName() {
        return this.domainName != null;
    }

    @Nullable
    @Override
    @Transient
    public Name getDomainName() {
        return this.domainName;
    }

    @Override
    public void setDomainName(@Nullable Name domainName) {
        this.domainName = domainName;
    }
}
