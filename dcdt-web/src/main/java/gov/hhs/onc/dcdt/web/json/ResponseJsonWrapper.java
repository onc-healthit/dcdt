package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.impl.RequestJsonWrapperImpl;
import javax.annotation.Nullable;

@JsonSubTypes({ @Type(RequestJsonWrapperImpl.class) })
public interface ResponseJsonWrapper<T extends ToolBean> extends JsonWrapper<T> {
    @JsonProperty("status")
    public ResponseStatus getStatus();

    public void setStatus(ResponseStatus status);

    @JsonProperty("errors")
    @Nullable
    public ErrorsJsonWrapper getErrors();

    public void setErrors(@Nullable ErrorsJsonWrapper errors);
}
