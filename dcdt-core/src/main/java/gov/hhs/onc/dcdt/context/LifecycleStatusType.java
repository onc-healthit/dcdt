package gov.hhs.onc.dcdt.context;

public enum LifecycleStatusType {
    STARTING, STARTED, STOPPING, STOPPED;

    public boolean isActive() {
        return (this != STOPPED);
    }

    public boolean isRunning() {
        return ((this == STARTED) || (this == STOPPING));
    }

    public boolean isTransitioning() {
        return ((this == STARTING) || (this == STOPPING));
    }
}
