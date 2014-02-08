package gov.hhs.onc.dcdt.web.json.impl;

import gov.hhs.onc.dcdt.beans.ToolBean;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.json.ToolBeanJsonDto;
import gov.hhs.onc.dcdt.web.json.JsonWrapper;
import java.util.ArrayList;
import java.util.List;

public abstract class AbstractJsonWrapper<T extends ToolBean, U extends ToolBeanJsonDto<T>> extends AbstractToolBean implements JsonWrapper<T, U> {
    protected List<U> items = new ArrayList<>();

    @Override
    public List<U> getItems() {
        return this.items;
    }

    @Override
    public void setItems(List<U> items) {
        this.items = items;
    }
}
