package gov.hhs.onc.dcdt.concurrent.impl;

import gov.hhs.onc.dcdt.concurrent.RejectedFutureHandler;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.concurrent.RunnableFuture;
import java.util.concurrent.ThreadPoolExecutor;

public abstract class AbstractRejectedFutureHandler<T, U extends RunnableFuture<T>> implements RejectedFutureHandler<T, U> {
    protected Class<T> resultClass;
    protected Class<U> futureClass;

    protected AbstractRejectedFutureHandler(Class<T> resultClass, Class<U> futureClass) {
        this.resultClass = resultClass;
        this.futureClass = futureClass;
    }

    @Override
    public void rejectedExecution(Runnable exec, ThreadPoolExecutor executor) {
        Class<? extends Runnable> execClass = exec.getClass();

        if (ToolClassUtils.isAssignable(execClass, this.futureClass)) {
            this.rejectedExecutionInternal(this.futureClass.cast(exec), executor);
        }
    }

    protected abstract void rejectedExecutionInternal(U future, ThreadPoolExecutor executor);
}
