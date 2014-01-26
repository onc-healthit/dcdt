package gov.hhs.onc.dcdt.data.types.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.ConversionService;

@SuppressWarnings({ "SpringJavaAutowiringInspection" })
public abstract class AbstractToolUserType<T, U, V, W extends AbstractSingleColumnStandardBasicType<V>> extends AbstractToolBean implements
    ToolUserType<T, U, V, W> {
    @Autowired
    protected ConversionService convService;

    protected Class<T> objClass;
    protected Class<U> objDbClass;
    protected W sqlTypeInstance;

    protected AbstractToolUserType(Class<T> objClass, Class<U> objDbClass, W sqlTypeInstance) {
        this.objClass = objClass;
        this.objDbClass = objDbClass;
        this.sqlTypeInstance = sqlTypeInstance;
    }

    @Override
    public String[] getKeys() {
        return ArrayUtils.toArray(ToolClassUtils.getName(this.objClass));
    }

    @Override
    public int[] sqlTypes() {
        return ArrayUtils.toPrimitive(ArrayUtils.toArray(this.sqlTypeInstance.sqlType()));
    }

    @Override
    public Class<?> returnedClass() {
        return this.objClass;
    }

    @Override
    public boolean equals(Object obj1, Object obj2) throws HibernateException {
        return Objects.equals(obj1, obj2);
    }

    @Override
    public int hashCode(Object obj) throws HibernateException {
        return Objects.hashCode(obj);
    }

    @Nullable
    @Override
    @SuppressWarnings({ "unchecked" })
    public Object nullSafeGet(ResultSet resultSet, String[] colNames, SessionImplementor session, Object ownerObj) throws HibernateException, SQLException {
        if (!this.canConvert(this.objDbClass, this.objClass)) {
            return null;
        }

        String colName;

        try {
            return (!ArrayUtils.isEmpty(colNames) && !StringUtils.isBlank(colName = colNames[0])) ? this.nullSafeGetInternal(resultSet,
                this.sqlTypeInstance.get(resultSet, colName, session), session, ownerObj) : null;
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to get user type (objDbClass=%s, objClass=%s, userTypeClass=%s, sqlTypeClass=%s).",
                ToolClassUtils.getName(this.objDbClass), ToolClassUtils.getName(this.objClass), ToolClassUtils.getName(this),
                ToolClassUtils.getName(this.sqlTypeInstance)), e);
        }
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void nullSafeSet(PreparedStatement prepStatement, @Nullable Object obj, int prepStatementParamIndex, SessionImplementor session)
        throws HibernateException, SQLException {
        if (!this.canConvert(this.objClass, this.objDbClass)) {
            return;
        }

        try {
            this.nullSafeSetInternal(prepStatement, obj, prepStatementParamIndex, session);
        } catch (Exception e) {
            throw new HibernateException(String.format("Unable to set user type (objClass=%s, objDbClass=%s, userTypeClass=%s, sqlTypeClass=%s).",
                ToolClassUtils.getName(this.objClass), ToolClassUtils.getName(this.objDbClass), ToolClassUtils.getName(this),
                ToolClassUtils.getName(this.sqlTypeInstance)), e);
        }
    }

    @Override
    public Object deepCopy(Object obj) throws HibernateException {
        return obj;
    }

    @Override
    public boolean isMutable() {
        return false;
    }

    @Nullable
    @Override
    public Serializable disassemble(Object obj) throws HibernateException {
        return null;
    }

    @Nullable
    @Override
    public Object assemble(Serializable cachedObj, Object ownerObj) throws HibernateException {
        return null;
    }

    @Override
    public Object replace(Object originalObj, Object targetObj, Object ownerObj) throws HibernateException {
        return originalObj;
    }

    @Nullable
    @SuppressWarnings({ "unchecked" })
    protected T nullSafeGetInternal(ResultSet resultSet, @Nullable Object objDb, SessionImplementor session, Object ownerObj) throws Exception {
        return (T) this.convert(objDb, this.objClass, session);
    }

    @SuppressWarnings({ "unchecked" })
    protected void nullSafeSetInternal(PreparedStatement prepStatement, @Nullable Object obj, int prepStatementParamIndex, SessionImplementor session)
        throws Exception {
        this.sqlTypeInstance.set(prepStatement, (V) this.convert(obj, this.objDbClass, session), prepStatementParamIndex, session);
    }

    @Nullable
    protected Object convert(@Nullable Object obj, Class<?> targetClass, SessionImplementor session) throws Exception {
        return this.convService.convert(obj, targetClass);
    }

    protected boolean canConvert(Class<?> sourceClass, Class<?> targetClass) {
        return this.convService.canConvert(sourceClass, targetClass);
    }
}
