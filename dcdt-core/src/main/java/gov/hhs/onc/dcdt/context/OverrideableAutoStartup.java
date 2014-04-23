package gov.hhs.onc.dcdt.context;

public interface OverrideableAutoStartup {
    public boolean isAutoStartup();

    public void setAutoStartup(boolean autoStartup);
}
