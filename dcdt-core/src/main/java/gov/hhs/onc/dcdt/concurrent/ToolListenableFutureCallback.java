package gov.hhs.onc.dcdt.concurrent;

import gov.hhs.onc.dcdt.beans.OverrideablePriorityOrdered;
import javax.annotation.Nullable;
import org.springframework.util.concurrent.ListenableFutureCallback;

public interface ToolListenableFutureCallback<T, U extends ToolListenableFutureTask<T>> extends ListenableFutureCallback<T>, OverrideablePriorityOrdered {
    public boolean hasTask();

    @Nullable
    public U getTask();

    public void setTask(@Nullable U task);
}
