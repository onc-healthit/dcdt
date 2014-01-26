package gov.hhs.onc.dcdt.data.types.impl;

import org.hibernate.type.StringType;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractStringUserType<T> extends AbstractToolUserType<T, String, String, StringType> {
    protected AbstractStringUserType(Class<T> objClass) {
        super(objClass, String.class, StringType.INSTANCE);
    }
}
