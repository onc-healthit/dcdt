package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolDescriptionBean;
import javax.annotation.Nullable;
import javax.persistence.MappedSuperclass;
import javax.persistence.Transient;
import org.apache.commons.lang3.StringUtils;

@MappedSuperclass
public abstract class AbstractToolDescriptionBean extends AbstractToolBean implements ToolDescriptionBean {
    protected String text;

    @Override
    public boolean hasText() {
        return !StringUtils.isBlank(this.text);
    }

    @Nullable
    @Override
    @Transient
    public String getText() {
        return this.text;
    }

    @Override
    public void setText(@Nullable String text) {
        this.text = text;
    }
}
