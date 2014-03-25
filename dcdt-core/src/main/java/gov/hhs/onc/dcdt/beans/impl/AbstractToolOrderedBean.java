package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils;
import org.springframework.core.Ordered;

public abstract class AbstractToolOrderedBean extends AbstractToolBean implements ToolOrderedBean {
    protected int order = Ordered.LOWEST_PRECEDENCE;

    @Override
    public int getOrder() {
        return ToolOrderUtils.getOrder(this, this.order);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }
}
