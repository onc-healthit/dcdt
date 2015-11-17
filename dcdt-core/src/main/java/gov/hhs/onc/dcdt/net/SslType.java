package gov.hhs.onc.dcdt.net;

import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum SslType implements ToolIdentifier {
    NONE(false), SSL(true), STARTTLS(true);

    private final boolean secure;

    private SslType(boolean secure) {
        this.secure = secure;
    }

    @Override
    public String getId() {
        return this.name();
    }

    public boolean isSecure() {
        return this.secure;
    }
}
