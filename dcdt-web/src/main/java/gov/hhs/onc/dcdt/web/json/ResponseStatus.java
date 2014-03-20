package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonRootName;

@JsonRootName("status")
public enum ResponseStatus {
    SUCCESS("success"), ERROR("error");

    private String statusName;

    ResponseStatus(String statusName) {
        this.statusName = statusName;
    }

    @Override
    public String toString() {
        return this.statusName;
    }

    public String getStatusName() {
        return this.statusName;
    }
}
