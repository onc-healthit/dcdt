package gov.hhs.onc.dcdt.concurrent.impl;

import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureCallback;
import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureTask;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils;
import javax.annotation.Nullable;
import org.springframework.core.Ordered;

public abstract class AbstractToolListenableFutureCallback<T, U extends ToolListenableFutureTask<T>> implements ToolListenableFutureCallback<T, U> {
    protected int order = Ordered.LOWEST_PRECEDENCE;
    protected U task;
    protected boolean done;
    protected boolean status;
    protected T result;
    protected Exception exception;

    protected AbstractToolListenableFutureCallback() {
        this(null);
    }

    protected AbstractToolListenableFutureCallback(@Nullable U task) {
        this.task = task;
    }

    @Override
    public void onSuccess(@Nullable T result) {
        this.result = result;
        this.status = true;
        this.done = true;

        this.onPreDone(result, null);
        this.onSuccessInternal(result);
        this.onPostDone(result, null);
    }

    @Override
    public void onFailure(Throwable exception) {
        this.exception = ((Exception) exception);
        this.status = false;
        this.done = true;

        this.onPreDone(null, exception);
        this.onFailureInternal(exception);
        this.onPostDone(null, exception);
    }

    @Override
    public boolean isDone() {
        return this.done;
    }

    protected void onSuccessInternal(@Nullable T result) {
    }

    protected void onFailureInternal(Throwable exception) {
    }

    protected void onPostDone(@Nullable T result, @Nullable Throwable exception) {
    }

    protected void onPreDone(@Nullable T result, @Nullable Throwable exception) {
    }

    @Override
    public boolean hasException() {
        return (this.exception != null);
    }

    @Nullable
    @Override
    public Exception getException() {
        return this.exception;
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
    public boolean hasResult() {
        return (this.result != null);
    }

    @Nullable
    @Override
    public T getResult() {
        return this.result;
    }

    @Override
    public boolean getStatus() {
        return this.status;
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
