package gov.hhs.onc.dcdt.service;

public enum ServiceStatusType {
    STARTING("starting", true, true), STARTED("started", true, false), STOPPING("stopping", true, true), STOPPED("stopped", false, false);

    private final String status;
    private final boolean running;
    private final boolean active;

    private ServiceStatusType(String status, boolean running, boolean active) {
        this.status = status;
        this.running = running;
        this.active = active;
    }

    public boolean isActive() {
        return this.active;
    }

    public boolean isRunning() {
        return this.running;
    }

    public String getStatus() {
        return this.status;
    }
}
