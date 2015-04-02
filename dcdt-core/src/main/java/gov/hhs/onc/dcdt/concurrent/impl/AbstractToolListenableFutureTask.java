package gov.hhs.onc.dcdt.concurrent.impl;

import gov.hhs.onc.dcdt.concurrent.ToolListenableFutureTask;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolOrderUtils.PriorityOrderedQueue;
import java.util.Queue;
import java.util.concurrent.Callable;
import javax.annotation.Nullable;
import org.springframework.util.concurrent.ListenableFutureCallback;
import org.springframework.util.concurrent.ListenableFutureTask;

public abstract class AbstractToolListenableFutureTask<T> extends ListenableFutureTask<T> implements ToolListenableFutureTask<T> {
    protected Queue<ListenableFutureCallback<? super T>> callbacks = new PriorityOrderedQueue<>();

    protected AbstractToolListenableFutureTask(Callable<T> callable) {
        super(callable);
    }

    protected AbstractToolListenableFutureTask(Runnable runnable, T result) {
        super(runnable, result);
    }

    @Override
    public void run() {
        this.callbacks.forEach(super::addCallback);

        super.run();
    }

    @Override
    public boolean hasCallbacks() {
        return !this.callbacks.isEmpty();
    }

    @Override
    public void addCallback(ListenableFutureCallback<? super T> callback) {
        this.callbacks.add(callback);
    }

    @Override
    public Queue<ListenableFutureCallback<? super T>> getCallbacks() {
        return this.callbacks;
    }

    @Override
    public void setCallbacks(@Nullable Queue<ListenableFutureCallback<? super T>> callbacks) {
        this.callbacks.clear();
        ToolCollectionUtils.addAll(this.callbacks, callbacks);
    }
}
