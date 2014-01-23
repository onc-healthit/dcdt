package gov.hhs.onc.dcdt.data.types.impl;

import org.hibernate.type.StringType;

public abstract class AbstractStringUserType<T> extends AbstractToolUserType<T, String, StringType> {
    protected AbstractStringUserType(Class<T> objClass) {
        super(objClass, String.class, StringType.INSTANCE);
    }
}
