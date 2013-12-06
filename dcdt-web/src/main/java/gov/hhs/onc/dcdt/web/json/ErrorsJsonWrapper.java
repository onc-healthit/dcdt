package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.annotation.JsonRootName;
import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.web.json.impl.ErrorsJsonWrapperImpl;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;

@JsonRootName("errors")
@JsonSubTypes({ @Type(ErrorsJsonWrapperImpl.class) })
public interface ErrorsJsonWrapper extends ToolBean {
    public boolean hasErrors();

    public boolean hasFieldErrors(String fieldName);

    public boolean hasFieldErrors();

    @Nullable
    public List<ErrorJsonWrapper> getFieldErrors(String fieldName);

    @JsonProperty(value = "fields")
    @Nullable
    public Map<String, List<ErrorJsonWrapper>> getFieldErrors();
    
    public void setFieldErrors(@Nullable Map<String, List<ErrorJsonWrapper>> fieldErrors);

    public boolean hasGlobalErrors();

    @JsonProperty(value = "global")
    @Nullable
    public List<ErrorJsonWrapper> getGlobalErrors();
    
    public void setGlobalErrors(@Nullable List<ErrorJsonWrapper> globalErrors);
}
