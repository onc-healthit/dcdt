package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.impl.ErrorJsonWrapperImpl;

@JsonRootName("error")
@JsonSubTypes({ @Type(ErrorJsonWrapperImpl.class) })
public interface ErrorJsonWrapper extends ToolBean {
    @JsonProperty(value = "message")
    public String getMessage();

    public void setMessage(String msg);

    @JsonProperty(value = "stackTrace")
    public String getStackTrace();

    public void setStackTrace(String stackTrace);
}
