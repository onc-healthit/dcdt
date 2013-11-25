package gov.hhs.onc.dcdt.web.json.impl;


import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.web.json.JsonWrapper;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJsonWrapper<T extends ToolBean> extends AbstractToolBean implements JsonWrapper<T> {
    protected List<T> items = new ArrayList<>();

    @Override
    public List<T> getItems() {
        return this.items;
    }

    @Override
    public void setItems(List<T> items) {
        this.items = items;
    }
}
