package gov.hhs.onc.dcdt.web.handler;

import gov.hhs.onc.dcdt.beans.ToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils.PriorityOrderedQueue;
import javax.annotation.Nullable;

public interface OrderedHandlerComponentComposite<T> extends ToolOrderedBean {
    public boolean hasHandlerComponents();

    public PriorityOrderedQueue<T> getHandlerComponents();

    public void setHandlerComponents(@Nullable Iterable<T> handlerComps);
}
