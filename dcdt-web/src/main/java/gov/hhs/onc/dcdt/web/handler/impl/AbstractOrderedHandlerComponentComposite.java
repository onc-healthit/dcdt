package gov.hhs.onc.dcdt.web.handler.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolOrderedBean;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils.PriorityOrderedQueue;
import gov.hhs.onc.dcdt.web.handler.OrderedHandlerComponentComposite;
import javax.annotation.Nullable;
import org.springframework.core.Ordered;

public abstract class AbstractOrderedHandlerComponentComposite<T> extends AbstractToolOrderedBean implements OrderedHandlerComponentComposite<T> {
    protected abstract static class AbstractOrderedHandlerComponentWrapper<T> extends AbstractToolOrderedBean {
        protected T handlerComp;

        protected AbstractOrderedHandlerComponentWrapper(T handlerComp) {
            this.handlerComp = handlerComp;
        }
    }

    protected PriorityOrderedQueue<T> handlerComps = new PriorityOrderedQueue<>();

    protected AbstractOrderedHandlerComponentComposite() {
        this(null);
    }

    protected AbstractOrderedHandlerComponentComposite(@Nullable Iterable<T> handlerComps) {
        this.setHandlerComponents(handlerComps);
    }

    protected abstract T wrapUnorderedHandlerComponent(T handlerComp);

    @Override
    public boolean hasHandlerComponents() {
        return !this.handlerComps.isEmpty();
    }

    @Override
    public PriorityOrderedQueue<T> getHandlerComponents() {
        return this.handlerComps;
    }

    @Override
    public void setHandlerComponents(@Nullable Iterable<T> handlerComps) {
        this.handlerComps.clear();

        if (handlerComps != null) {
            for (T handlerComp : handlerComps) {
                if (!(handlerComp instanceof Ordered)) {
                    handlerComp = this.wrapUnorderedHandlerComponent(handlerComp);
                }

                this.handlerComps.add(handlerComp);
            }
        }
    }
}
