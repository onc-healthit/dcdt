package gov.hhs.onc.dcdt.beans;

import gov.hhs.onc.dcdt.context.LifecycleStatusType;
import gov.hhs.onc.dcdt.context.OverrideableAutoStartup;
import gov.hhs.onc.dcdt.context.OverrideablePhased;
import javax.annotation.Nullable;
import org.springframework.context.SmartLifecycle;

public interface ToolLifecycleBean extends OverrideableAutoStartup, OverrideablePhased, SmartLifecycle, ToolBean {
    @Override
    public void stop(@Nullable Runnable stopCallback);

    public boolean canStop();

    public boolean canStart();

    public LifecycleStatusType getLifecycleStatus();
}
