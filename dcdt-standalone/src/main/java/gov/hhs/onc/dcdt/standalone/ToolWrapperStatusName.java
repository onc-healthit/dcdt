package gov.hhs.onc.dcdt.standalone;

public enum ToolWrapperStatusName {
    STARTING("starting"), STARTED("started"), STOPPING("stopping"), STOPPED("stopped");

    private String name;

    ToolWrapperStatusName(String name) {
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
