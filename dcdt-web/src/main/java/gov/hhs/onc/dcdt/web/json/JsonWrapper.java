package gov.hhs.onc.dcdt.web.json;


import com.fasterxml.jackson.annotation.JsonProperty;
import gov.hhs.onc.dcdt.beans.ToolBean;
import java.util.List;

public interface JsonWrapper<T extends ToolBean> extends ToolBean {
    @JsonProperty("items")
    public List<T> getItems();

    public void setItems(List<T> items);
}
