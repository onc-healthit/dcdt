package gov.hhs.onc.dcdt.data.types;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.io.Serializable;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javax.annotation.Nullable;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.AbstractSingleColumnStandardBasicType;
import org.hibernate.type.DiscriminatorType;
import org.hibernate.usertype.UserType;

public interface ToolUserType<T, U, V extends AbstractSingleColumnStandardBasicType<U> & DiscriminatorType<U>> extends ToolBean, UserType {
    public String[] getKeys();

    @Override
    public Class<?> returnedClass();

    @Override
    public boolean equals(Object obj1, Object obj2) throws HibernateException;

    @Override
    public int hashCode(Object obj) throws HibernateException;

    @Nullable
    @Override
    public Object nullSafeGet(ResultSet resultSet, String[] colNames, SessionImplementor session, Object ownerObj) throws HibernateException, SQLException;

    @Override
    public void nullSafeSet(PreparedStatement prepStatement, @Nullable Object obj, int prepStatementParamIndex, SessionImplementor session)
        throws HibernateException, SQLException;

    @Nullable
    @Override
    public Serializable disassemble(Object obj) throws HibernateException;

    @Nullable
    @Override
    public Object assemble(Serializable cachedObj, Object ownerObj) throws HibernateException;
}
