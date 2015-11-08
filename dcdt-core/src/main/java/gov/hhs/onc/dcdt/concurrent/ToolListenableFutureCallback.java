package gov.hhs.onc.dcdt.concurrent;

import gov.hhs.onc.dcdt.beans.OverrideablePriorityOrdered;
import javax.annotation.Nullable;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface ToolListenableFutureCallback<T, U extends ToolListenableFutureTask<T>> extends ListenableFutureCallback<T>, OverrideablePriorityOrdered {
    public boolean isDone();

    public boolean hasException();

    @Nullable
    public Exception getException();

    public boolean hasResult();

    @Nullable
    public T getResult();

    public boolean hasTask();

    public boolean getStatus();

    @Nullable
    public U getTask();

    public void setTask(@Nullable U task);
}
