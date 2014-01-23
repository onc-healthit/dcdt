package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolNamedBean;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public abstract class AbstractToolNamedBean extends AbstractToolBean implements ToolNamedBean {
    protected String name;
    protected String nameDisplay;

    @Override
    public boolean hasName() {
        return !StringUtils.isBlank(this.name);
    }

    @Nullable
    @Override
    @Transient
    public String getName() {
        return this.name;
    }

    @Override
    public void setName(@Nullable String name) {
        this.name = name;
    }

    @Override
    public boolean hasNameDisplay() {
        return !StringUtils.isBlank(this.nameDisplay);
    }

    @Nullable
    @Override
    @Transient
    public String getNameDisplay() {
        return this.nameDisplay;
    }

    @Override
    public void setNameDisplay(@Nullable String nameDisplay) {
        this.nameDisplay = nameDisplay;
    }
}
