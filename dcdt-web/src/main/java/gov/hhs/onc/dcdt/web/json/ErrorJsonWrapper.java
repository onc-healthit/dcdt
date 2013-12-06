package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.impl.ErrorJsonWrapperImpl;
import java.util.List;

@JsonRootName("error")
@JsonSubTypes({ @Type(ErrorJsonWrapperImpl.class) })
public interface ErrorJsonWrapper extends ToolBean {
    public boolean hasMessages();

    @JsonProperty(value = "messages")
    public List<String> getMessages();

    public void setMessages(List<String> msgs);

    public boolean hasStackTrace();

    @JsonProperty(value = "stackTrace")
    public String getStackTrace();

    public void setStackTrace(String stackTrace);
}
