package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolDescriptorBean;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractToolDescriptorBean extends AbstractToolBean implements ToolDescriptorBean {
    protected void reset() {
    }
}
