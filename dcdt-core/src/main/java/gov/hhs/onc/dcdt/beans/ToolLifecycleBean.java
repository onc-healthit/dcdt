package gov.hhs.onc.dcdt.beans;

import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import javax.annotation.Nullable;
import org.springframework.context.SmartLifecycle;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

public interface ToolLifecycleBean extends SmartLifecycle, ToolBean {
    @Override
    public void stop(@Nullable Runnable stopCallback);

    public boolean canStop();

    public boolean canStart();

    public void setAutoStartup(boolean autoStartup);

    public LifecycleStatusType getLifecycleStatus();

    public void setPhase(int phase);

    public ThreadPoolTaskExecutor getTaskExecutor();

    public void setTaskExecutor(ThreadPoolTaskExecutor taskExec);
}
