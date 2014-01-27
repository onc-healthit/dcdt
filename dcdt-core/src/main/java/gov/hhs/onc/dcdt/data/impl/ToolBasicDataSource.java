package gov.hhs.onc.dcdt.data.impl;

import ch.qos.logback.classic.jul.JULHelper;
import java.sql.SQLException;
import java.sql.SQLFeatureNotSupportedException;
import java.util.Objects;
import java.util.Properties;
import java.util.logging.Logger;
import org.apache.commons.collections4.MapUtils;
import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.derby.iapi.reference.Attribute;
import org.apache.derby.jdbc.EmbeddedDriver;
import org.apache.derby.shared.common.reference.SQLState;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.DisposableBean;

public class ToolBasicDataSource extends BasicDataSource implements DisposableBean {
    private final static String DELIM_CONN_PROP = ";";
    private final static String DELIM_CONN_PROP_VALUE = "=";

    private final static String SQL_STATE_SHUTDOWN = StringUtils.split(SQLState.SHUTDOWN_DATABASE, ".", 2)[0];
    private final static Properties CONN_PROPS_SHUTDOWN = (Properties) MapUtils.putAll(new Properties(),
        ArrayUtils.toArray(ArrayUtils.toArray(Attribute.SHUTDOWN_ATTR, Boolean.toString(true))));

    private final static org.slf4j.Logger LOGGER = LoggerFactory.getLogger(ToolBasicDataSource.class);

    @Override
    public synchronized void destroy() throws Exception {
        this.close();

        try {
            new EmbeddedDriver().connect(this.url, CONN_PROPS_SHUTDOWN);

            LOGGER.info(String.format("Shutdown Derby database (url=%s).", this.url));
        } catch (SQLException e) {
            if (!Objects.equals(e.getSQLState(), SQL_STATE_SHUTDOWN)) {
                LOGGER.warn(String.format("Unable to shutdown Derby database (url=%s).", this.url), e);
            }
        }
    }

    public void setConnectionPropertiesObject(Properties connProps) {
        StrBuilder connPropsStrBuilder = new StrBuilder();

        for (String connPropName : connProps.stringPropertyNames()) {
            connPropsStrBuilder.appendSeparator(DELIM_CONN_PROP);
            connPropsStrBuilder.append(connPropName);
            connPropsStrBuilder.appendSeparator(DELIM_CONN_PROP_VALUE);
            connPropsStrBuilder.append(connProps.getProperty(connPropName));
        }

        this.setConnectionProperties(connPropsStrBuilder.build());
    }

    @Override
    public Logger getParentLogger() throws SQLFeatureNotSupportedException {
        return JULHelper.asJULLogger(LOGGER.getName());
    }
}
