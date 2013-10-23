package gov.hhs.onc.dcdt.service.wrapper;

public enum ToolServiceWrapperStatusName {
    STARTING("starting"), STARTED("started"), STOPPING("stopping"), STOPPED("stopped");

    private String name;

    ToolServiceWrapperStatusName(String name) {
        this.name = name;
    }

    @Override
    public String toString() {
        return this.name;
    }

    public String getName() {
        return this.name;
    }
}
