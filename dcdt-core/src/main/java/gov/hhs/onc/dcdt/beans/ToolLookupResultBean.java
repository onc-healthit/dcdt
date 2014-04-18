package gov.hhs.onc.dcdt.beans;

import java.io.Serializable;

public interface ToolLookupResultBean<T extends Serializable, U extends Enum<U>> extends Iterable<T>, ToolResultBean {
    public U getType();
}
