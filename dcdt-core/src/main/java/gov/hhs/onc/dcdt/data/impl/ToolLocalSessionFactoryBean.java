package gov.hhs.onc.dcdt.data.impl;

import com.mchange.v2.c3p0.C3P0Registry;
import com.mchange.v2.c3p0.ComboPooledDataSource;
import com.mchange.v2.c3p0.PooledDataSource;
import gov.hhs.onc.dcdt.convert.ToolConverter;
import gov.hhs.onc.dcdt.data.types.ToolUserType;
import gov.hhs.onc.dcdt.utils.ToolBeanFactoryUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.sql.SQLException;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Properties;
import java.util.Set;
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
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.context.support.AbstractApplicationContext;
import org.springframework.orm.hibernate4.LocalSessionFactoryBean;
import org.springframework.orm.hibernate4.LocalSessionFactoryBuilder;

public class ToolLocalSessionFactoryBean extends LocalSessionFactoryBean implements ApplicationContextAware {
    private final static Properties CONN_PROPS_SHUTDOWN = (Properties) MapUtils.putAll(
        new Properties(),
        ArrayUtils.toArray(ArrayUtils.toArray(Attribute.SHUTDOWN_ATTR, Boolean.toString(true)),
            ArrayUtils.toArray(Attribute.DEREGISTER_ATTR, Boolean.toString(true))));
    private final static String SQL_STATE_SHUTDOWN = StringUtils.split(SQLState.SHUTDOWN_DATABASE, ".", 2)[0];

    private final static Logger LOGGER = LoggerFactory.getLogger(ToolLocalSessionFactoryBean.class);

    private AbstractApplicationContext appContext;
    private Set<ToolConverter> convs = new LinkedHashSet<>();
    private ComboPooledDataSource dataSource;

    @Override
    @SuppressWarnings({ "ConstantConditions" })
    protected SessionFactory buildSessionFactory(LocalSessionFactoryBuilder localSessionFactoryBuilder) {
        ToolUserType<?, ?, ?, ?> convUserType;

        for (ToolConverter conv : this.convs) {
            if (conv.hasUserTypeClass() && ((convUserType = ToolBeanFactoryUtils.getBeanOfType(this.appContext, conv.getUserTypeClass())) != null)) {
                localSessionFactoryBuilder.registerTypeOverride(convUserType, convUserType.getKeys());
            }
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
            if (!Objects.equals(e.getSQLState(), SQL_STATE_SHUTDOWN)) {
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
    public void setApplicationContext(ApplicationContext appContext) throws BeansException {
        this.appContext = (AbstractApplicationContext) appContext;
    }

    public Set<ToolConverter> getConverters() {
        return this.convs;
    }

    public void setConverters(Set<ToolConverter> convs) {
        this.convs = convs;
    }

    @Override
    public void setDataSource(DataSource dataSource) {
        super.setDataSource(this.dataSource = (ComboPooledDataSource) dataSource);
    }
}
