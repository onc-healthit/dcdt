package gov.hhs.onc.dcdt.data.events.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.data.events.ToolBeanEntityInterceptor;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils.PriorityOrderedQueue;
import java.io.Serializable;
import java.util.List;
import java.util.Queue;
import javax.annotation.Nullable;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component("compositeBeanInterceptor")
public class CompositeBeanEntityInterceptor extends AbstractToolBeanDataInterceptor {
    private final static long serialVersionUID = 0L;

    private Queue<ToolBeanEntityInterceptor<? extends ToolBean>> beanEntityInterceptors;

    @Override
    public void onDelete(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        for (ToolBeanEntityInterceptor<? extends ToolBean> beanEntityInterceptor : this.beanEntityInterceptors) {
            beanEntityInterceptor.onDelete(entity, id, state, propertyNames, types);
        }
    }

    @Override
    public boolean onLoad(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        for (ToolBeanEntityInterceptor<? extends ToolBean> beanEntityInterceptor : this.beanEntityInterceptors) {
            if (beanEntityInterceptor.onLoad(entity, id, state, propertyNames, types)) {
                return true;
            }
        }

        return super.onLoad(entity, id, state, propertyNames, types);
    }

    @Override
    public boolean onSave(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        for (ToolBeanEntityInterceptor<? extends ToolBean> beanEntityInterceptor : this.beanEntityInterceptors) {
            if (beanEntityInterceptor.onSave(entity, id, state, propertyNames, types)) {
                return true;
            }
        }

        return super.onSave(entity, id, state, propertyNames, types);
    }

    @Override
    public void afterTransactionBegin(Transaction tx) {
        for (ToolBeanEntityInterceptor<? extends ToolBean> beanEntityInterceptor : beanEntityInterceptors) {
            beanEntityInterceptor.afterTransactionBegin(tx);
        }
    }

    @Override
    public void afterTransactionCompletion(Transaction tx) {
        for (ToolBeanEntityInterceptor<? extends ToolBean> beanEntityInterceptor : beanEntityInterceptors) {
            beanEntityInterceptor.afterTransactionCompletion(tx);
        }
    }

    @Autowired(required = false)
    private void setBeanEntityInterceptors(@Nullable List<ToolBeanEntityInterceptor<? extends ToolBean>> beanEntityInterceptors) {
        this.beanEntityInterceptors =
            ToolCollectionUtils.addAll(new PriorityOrderedQueue<ToolBeanEntityInterceptor<? extends ToolBean>>(), beanEntityInterceptors);
    }
}
