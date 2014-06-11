package gov.hhs.onc.dcdt.concurrent;

import java.util.Queue;
import java.util.concurrent.RunnableFuture;
import javax.annotation.Nullable;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface ToolListenableFutureTask<T> extends ListenableFuture<T>, RunnableFuture<T> {
    public boolean hasCallbacks();

    public Queue<ListenableFutureCallback<? super T>> getCallbacks();

    public void setCallbacks(@Nullable Queue<ListenableFutureCallback<? super T>> callbacks);
}
