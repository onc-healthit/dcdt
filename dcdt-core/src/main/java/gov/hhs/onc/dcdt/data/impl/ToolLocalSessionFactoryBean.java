package gov.hhs.onc.dcdt.data.impl;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Properties;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.sql.DataSource;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.derby.iapi.reference.Attribute;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.derby.shared.common.reference.SQLState;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;
import org.springframework.orm.hibernate5.LocalSessionFactoryBuilder;

public class ToolLocalSessionFactoryBean extends LocalSessionFactoryBean {
    private final static Properties CONN_PROPS_SHUTDOWN = (Properties) MapUtils.putAll(
        new Properties(),
        ArrayUtils.toArray(ArrayUtils.toArray(Attribute.SHUTDOWN_ATTR, Boolean.toString(true)),
            ArrayUtils.toArray(Attribute.DEREGISTER_ATTR, Boolean.toString(true))));
    private final static Set<String> SHUTDOWN_SQL_STATES_IGNORE = Stream.of(SQLState.DATABASE_NOT_FOUND, SQLState.SHUTDOWN_DATABASE)
        .map(sqlState -> StringUtils.split(sqlState, ".", 2)[0]).collect(Collectors.toSet());

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolLocalSessionFactoryBean.class);

    private Set<ToolUserType<?, ?, ?, ?>> userTypes = new LinkedHashSet<>();
    private ComboPooledDataSource dataSource;

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder localSessionFactoryBuilder) {
        for (ToolUserType<?, ?, ?, ?> userType : this.userTypes) {
            localSessionFactoryBuilder.registerTypeOverride(userType, userType.getKeys());
        }

        return super.buildSessionFactory(localSessionFactoryBuilder);
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public void destroy() {
        String dataSourceUrl = this.dataSource.getJdbcUrl();

        try {
            new EmbeddedDriver().connect(dataSourceUrl, CONN_PROPS_SHUTDOWN);

            LOGGER.info(String.format("Shutdown and de-registered Derby database (url=%s).", dataSourceUrl));
        } catch (SQLException e) {
            if (!SHUTDOWN_SQL_STATES_IGNORE.contains(e.getSQLState())) {
                LOGGER.warn(String.format("Unable to shutdown Derby database (url=%s).", dataSourceUrl), e);
            }
        }

        super.destroy();

        for (PooledDataSource pooledDataSource : (Set<PooledDataSource>) C3P0Registry.getPooledDataSources()) {
            try {
                pooledDataSource.close();

                LOGGER.info(String.format("Destroyed C3P0 pooled data source (class=%s).", ToolClassUtils.getName(pooledDataSource)));
            } catch (SQLException e) {
                LOGGER.warn(String.format("Unable to destroy C3P0 pooled data source (class=%s).", ToolClassUtils.getName(pooledDataSource)), e);
            }
        }
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(this.dataSource = (ComboPooledDataSource) dataSource);
    }

    public Set<ToolUserType<?, ?, ?, ?>> getUserTypes() {
        return this.userTypes;
    }

    public void setUserTypes(Set<ToolUserType<?, ?, ?, ?>> userTypes) {
        this.userTypes = userTypes;
    }
}
