package gov.hhs.onc.dcdt.standalone;

public class ToolWrapperStatus {
    private ToolWrapperStatusName status;
    private long pid;

    public ToolWrapperStatus() {
        this(ToolWrapperStatusName.STOPPED, -1);
    }

    public ToolWrapperStatus(ToolWrapperStatusName status, long pid) {
        this.status = status;
        this.pid = pid;
    }

    public long getPid() {
        return this.pid;
    }

    public void setPid(long pid) {
        this.pid = pid;
    }

    public ToolWrapperStatusName getStatus() {
        return this.status;
    }

    public void setStatus(ToolWrapperStatusName status) {
        this.status = status;
    }
}
