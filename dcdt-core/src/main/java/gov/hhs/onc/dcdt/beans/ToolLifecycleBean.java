package gov.hhs.onc.dcdt.beans;

import org.springframework.context.SmartLifecycle;

public interface ToolLifecycleBean extends OverrideableAutoStartup, OverrideablePhased, SmartLifecycle, ToolBean {
}
