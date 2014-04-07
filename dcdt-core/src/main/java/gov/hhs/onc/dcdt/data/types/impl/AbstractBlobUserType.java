package gov.hhs.onc.dcdt.data.types.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.sql.Blob;
import javax.annotation.Nullable;
import org.apache.commons.io.IOUtils;
import org.hibernate.Hibernate;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BlobType;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractBlobUserType<T> extends AbstractToolUserType<T, byte[], Blob, BlobType> {
    protected AbstractBlobUserType(Class<T> objClass) {
        super(objClass, byte[].class, BlobType.INSTANCE);
    }

    @Nullable
    @Override
    protected Object convert(@Nullable Object source, Class<?> targetClass, SessionImplementor session) throws Exception {
        if (source == null) {
            return null;
        } else if (ToolClassUtils.isAssignable(targetClass, this.objClass)) {
            return super.convert(IOUtils.toByteArray(((Blob) source).getBinaryStream()), targetClass, session);
        } else {
            return Hibernate.getLobCreator(session).createBlob((byte[]) super.convert(source, targetClass, session));
        }
    }
}
