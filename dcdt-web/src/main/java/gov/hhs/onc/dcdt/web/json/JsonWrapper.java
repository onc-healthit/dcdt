package gov.hhs.onc.dcdt.web.json;

import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import java.util.List;
import javax.validation.Valid;

public interface JsonWrapper<T extends ToolBean, U extends ToolBeanJsonDto<T>> extends ToolBean {
    @JsonProperty("items")
    @Valid
    public List<U> getItems();

    public void setItems(List<U> items);
}
