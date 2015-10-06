package gov.hhs.onc.dcdt.data.events.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.events.ToolBeanEntityEventType;
import gov.hhs.onc.dcdt.data.events.ToolBeanEntityInterceptor;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils;
import java.io.Serializable;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.core.Ordered;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolBeanEntityInterceptor<T extends ToolBean> extends AbstractToolBeanDataInterceptor implements ToolBeanEntityInterceptor<T> {
    protected int order = Ordered.LOWEST_PRECEDENCE;
    protected Class<T> beanEntityClass;

    private final static long serialVersionUID = 0L;

    protected AbstractToolBeanEntityInterceptor(Class<T> beanEntityClass) {
        this.beanEntityClass = beanEntityClass;
    }

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        if (this.canInterceptBeanEntityEvent(ToolBeanEntityEventType.DELETE, entity, id, state, propertyNames, types)) {
            this.onDeleteInternal(entity, id, state, propertyNames, types);
        }
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return this.canInterceptBeanEntityEvent(ToolBeanEntityEventType.LOAD, entity, id, state, propertyNames, types) ? this.onLoadInternal(entity, id, state,
            propertyNames, types) : super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return this.canInterceptBeanEntityEvent(ToolBeanEntityEventType.SAVE, entity, id, state, propertyNames, types) ? this.onSaveInternal(entity, id, state,
            propertyNames, types) : super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        if (this.canInterceptBeanEntityTransactionEvent(ToolBeanEntityEventType.TX_BEGIN, tx)) {
            this.afterTransactionBeginInternal(tx);
        }
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        if (this.canInterceptBeanEntityTransactionEvent(ToolBeanEntityEventType.TX_COMPLETE, tx)) {
            this.afterTransactionCompletionInternal(tx);
        }
    }

    protected void onDeleteInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
    }

    protected boolean onLoadInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onLoad(entity, id, state, propertyNames, types);
    }

    protected boolean onSaveInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        return super.onSave(entity, id, state, propertyNames, types);
    }

    protected void afterTransactionBeginInternal(Transaction tx) {
    }

    protected void afterTransactionCompletionInternal(Transaction tx) {
    }

    protected boolean canInterceptBeanEntityEvent(ToolBeanEntityEventType beanEntityEventType, Object entity, Serializable id, Object[] state,
        String[] propertyNames, Type[] types) {
        return ToolClassUtils.isAssignable(this.beanEntityClass, ToolClassUtils.getClass(entity));
    }

    protected boolean canInterceptBeanEntityTransactionEvent(ToolBeanEntityEventType beanEntityEventType, Transaction tx) {
        return true;
    }

    @Override
    public int getOrder() {
        return ToolOrderUtils.getOrder(this, this.order);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }
}
