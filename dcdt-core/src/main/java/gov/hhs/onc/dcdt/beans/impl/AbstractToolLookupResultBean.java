package gov.hhs.onc.dcdt.beans.impl;

import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import java.io.Serializable;

public abstract class AbstractToolLookupResultBean<T extends Serializable, U extends Enum<U>> extends AbstractToolResultBean implements
    ToolLookupResultBean<T, U> {
}
