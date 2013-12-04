package gov.hhs.onc.dcdt.service.wrapper;

public class ToolServiceWrapperStatus {
    private ToolServiceWrapperStatusName status;
    private long pid;

    public ToolServiceWrapperStatus() {
        this(ToolServiceWrapperStatusName.STOPPED, -1);
    }

    public ToolServiceWrapperStatus(ToolServiceWrapperStatusName status, long pid) {
        this.status = status;
        this.pid = pid;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public ToolServiceWrapperStatusName getStatus() {
        return this.status;
    }

    public void setStatus(ToolServiceWrapperStatusName status) {
        this.status = status;
    }
}
