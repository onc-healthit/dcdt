package gov.hhs.onc.dcdt.data.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.io.Serializable;
import org.hibernate.Transaction;
import org.hibernate.type.Type;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.annotation.Scope;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component("loggingBeanEntityInterceptor")
@Order(Ordered.LOWEST_PRECEDENCE)
@Scope("singleton")
public class LoggingBeanEntityInterceptor extends AbstractToolBeanEntityInterceptor<ToolBean> {
    private final static String EVENT_ITEM_DELIM = ",";

    private final static Logger LOGGER = LoggerFactory.getLogger(LoggingBeanEntityInterceptor.class);

    private final static long serialVersionUID = 0L;

    public LoggingBeanEntityInterceptor() {
        super(ToolBean.class);
    }

    @Override
    protected void onDeleteInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        LOGGER.trace(String.format("Bean entity (class=%s, id=%s, propNames=[%s], types=[%s]) is being deleted: state=[%s]", ToolClassUtils.getName(entity),
            id, ToolStringUtils.joinDelimit(propertyNames, EVENT_ITEM_DELIM), ToolStringUtils.joinDelimit(getTypeNames(types), EVENT_ITEM_DELIM),
            ToolStringUtils.joinDelimit(state, EVENT_ITEM_DELIM)));

        super.onDeleteInternal(entity, id, state, propertyNames, types);
    }

    @Override
    protected boolean onLoadInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        LOGGER.trace(String.format("Bean entity (class=%s, id=%s, propNames=[%s], types=[%s]) is being loaded: state=[%s]", ToolClassUtils.getName(entity), id,
            ToolStringUtils.joinDelimit(propertyNames, EVENT_ITEM_DELIM), ToolStringUtils.joinDelimit(getTypeNames(types), EVENT_ITEM_DELIM),
            ToolStringUtils.joinDelimit(state, EVENT_ITEM_DELIM)));

        return super.onLoadInternal(entity, id, state, propertyNames, types);
    }

    @Override
    protected boolean onSaveInternal(Object entity, Serializable id, Object[] state, String[] propertyNames, Type[] types) {
        LOGGER.trace(String.format("Bean entity (class=%s, id=%s, propNames=[%s], types=[%s]) is being saved: state=[%s]", ToolClassUtils.getName(entity), id,
            ToolStringUtils.joinDelimit(propertyNames, EVENT_ITEM_DELIM), ToolStringUtils.joinDelimit(getTypeNames(types), EVENT_ITEM_DELIM),
            ToolStringUtils.joinDelimit(state, EVENT_ITEM_DELIM)));

        return super.onSaveInternal(entity, id, state, propertyNames, types);
    }

    @Override
    protected void afterTransactionBeginInternal(Transaction tx) {
        LOGGER.trace(String.format("Bean entity transaction (initiator=%s) begun.", tx.isInitiator()));

        super.afterTransactionBeginInternal(tx);
    }

    @Override
    protected void afterTransactionCompletionInternal(Transaction tx) {
        LOGGER.trace(String.format("Bean entity transaction (initiator=%s) completed: committed=%s, rolledBack=%s", tx.isInitiator(), tx.wasCommitted(),
            tx.wasRolledBack()));

        super.afterTransactionCompletionInternal(tx);
    }

    private static String[] getTypeNames(Type[] types) {
        int numTypes = types.length;
        String[] typeNames = new String[numTypes];

        for (int a = 0; a < numTypes; a++) {
            typeNames[a] = types[a].getName();
        }

        return typeNames;
    }
}
