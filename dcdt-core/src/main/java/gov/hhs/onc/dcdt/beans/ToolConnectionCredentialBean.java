package gov.hhs.onc.dcdt.beans;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Nullable;

public interface ToolConnectionCredentialBean<T, U> extends ToolNamedBean {
    public boolean hasId();

    @JsonProperty("id")
    @Nullable
    public T getId();

    public void setId(@Nullable T id);

    public boolean hasSecret();

    @Nullable
    public U getSecret();

    public void setSecret(@Nullable U secret);
}
