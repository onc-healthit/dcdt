package gov.hhs.onc.dcdt.web.json;


import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.impl.RequestJsonWrapperImpl;
import java.util.List;

@JsonRootName("response")
@JsonSubTypes({ @Type(RequestJsonWrapperImpl.class) })
public interface ResponseJsonWrapper<T extends ToolBean> extends JsonWrapper<T> {
    @JsonProperty("errors")
    public List<ErrorJsonWrapper> getErrors();

    public void setErrors(List<ErrorJsonWrapper> errors);
}
