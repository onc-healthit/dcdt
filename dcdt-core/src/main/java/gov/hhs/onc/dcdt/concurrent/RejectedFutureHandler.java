package gov.hhs.onc.dcdt.concurrent;

import java.util.concurrent.RejectedExecutionHandler;
import java.util.concurrent.RunnableFuture;

public interface RejectedFutureHandler<T, U extends RunnableFuture<T>> extends RejectedExecutionHandler {
}
