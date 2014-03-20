package gov.hhs.onc.dcdt.web.context.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.sql.Driver;
import java.sql.DriverManager;
import java.sql.SQLException;
import javax.servlet.ServletContextEvent;
import javax.servlet.annotation.WebListener;
import org.apache.commons.collections4.EnumerationUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@WebListener("driverManagerServletContextListener")
public class DriverManagerServletContextListener extends AbstractToolServletContextListener {
    private final static Logger LOGGER = LoggerFactory.getLogger(DriverManagerServletContextListener.class);

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        for (Driver driver : EnumerationUtils.toList(DriverManager.getDrivers())) {
            try {
                DriverManager.deregisterDriver(driver);

                LOGGER.info(String.format("De-registered JDBC driver (class=%s).", ToolClassUtils.getName(driver)));
            } catch (SQLException e) {
                LOGGER.warn(String.format("Unable to de-register JDBC driver (class=%s).", ToolClassUtils.getName(driver)), e);
            }
        }
    }
}
