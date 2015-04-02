package gov.hhs.onc.dcdt.concurrent.impl;

import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureTask;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils;
import javax.annotation.Nullable;
import org.springframework.core.Ordered;

public abstract class AbstractToolListenableFutureCallback<T, U extends ToolListenableFutureTask<T>> implements ToolListenableFutureCallback<T, U> {
    protected int order = Ordered.LOWEST_PRECEDENCE;
    protected U task;

    protected AbstractToolListenableFutureCallback() {
        this(null);
    }

    protected AbstractToolListenableFutureCallback(@Nullable U task) {
        this.task = task;
    }

    @Override
    public void onSuccess(@Nullable T result) {
        this.onPreDone(true, result, null);
        this.onSuccessInternal(result);
        this.onPostDone(true, result, null);
    }

    @Override
    public void onFailure(Throwable th) {
        this.onPreDone(false, null, th);
        this.onFailureInternal(th);
        this.onPostDone(false, null, th);
    }

    protected void onSuccessInternal(@Nullable T result) {
    }

    protected void onFailureInternal(Throwable th) {
    }

    protected void onPostDone(boolean status, @Nullable T result, @Nullable Throwable th) {
    }

    protected void onPreDone(boolean status, @Nullable T result, @Nullable Throwable th) {
    }

    @Override
    public int getOrder() {
        return ToolOrderUtils.getOrder(this, this.order);
    }

    @Override
    public void setOrder(int order) {
        this.order = order;
    }

    @Override
    public boolean hasTask() {
        return (this.task != null);
    }

    @Nullable
    @Override
    public U getTask() {
        return this.task;
    }

    @Override
    public void setTask(@Nullable U task) {
        this.task = task;
    }
}
