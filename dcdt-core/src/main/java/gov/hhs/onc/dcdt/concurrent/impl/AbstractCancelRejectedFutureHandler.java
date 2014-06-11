package gov.hhs.onc.dcdt.concurrent.impl;

import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractCancelRejectedFutureHandler<T, U extends RunnableFuture<T>> extends AbstractRejectedFutureHandler<T, U> {
    protected AbstractCancelRejectedFutureHandler(Class<T> resultClass, Class<U> futureClass) {
        super(resultClass, futureClass);
    }

    @Override
    protected void rejectedExecutionInternal(U future, ThreadPoolExecutor executor) {
        future.cancel(true);
    }
}
