package gov.hhs.onc.dcdt.testcases.hosting.results.impl;

import com.fasterxml.jackson.annotation.JsonTypeName;
import gov.hhs.onc.dcdt.testcases.hosting.results.HostingTestcaseResultInfo;
import gov.hhs.onc.dcdt.testcases.results.impl.AbstractToolTestcaseResultDescriptor;
import org.apache.commons.lang3.StringUtils;
import javax.annotation.Nullable;

@JsonTypeName("hostingTestcaseResultInfo")
public class HostingTestcaseResultInfoImpl extends AbstractToolTestcaseResultDescriptor implements HostingTestcaseResultInfo {
    private boolean successful;
    private String message;
    private String certStr;

    @Override
    public boolean isSuccessful() {
        return this.successful;
    }

    @Override
    public void setSuccessful(boolean successful) {
        this.successful = successful;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.message);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.message;
    }

    @Override
    public void setMessage(@Nullable String message) {
        this.message = message;
    }

    @Nullable
    @Override
    public String getCertificate() {
        return this.certStr;
    }

    @Override
    public void setCertificate(@Nullable String certStr) {
        this.certStr = certStr;
    }
}
