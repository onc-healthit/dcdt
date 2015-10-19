package gov.hhs.onc.dcdt.logging.impl;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferConfigurator;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.Configurator;
import ch.qos.logback.classic.spi.LoggerContextAwareBase;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.context.utils.ToolContextUtils;
import gov.hhs.onc.dcdt.logging.LoggingInitializer;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.ApplicationContextException;

public class LoggingConfigurator extends LoggerContextAwareBase implements Configurator, LoggerContextListener {
    private static class DefaultLoggingInitializer extends AbstractLoggingInitializer {
        public DefaultLoggingInitializer() {
            super("dcdt-core", "core");
        }
    }

    @Override
    public void configure(LoggerContext loggerContext) {
        loggerContext.addListener(this);

        LevelChangePropagator lvlChangePropagator = new LevelChangePropagator();
        lvlChangePropagator.setContext(loggerContext);
        lvlChangePropagator.setResetJUL(true);
        loggerContext.addListener(lvlChangePropagator);

        LoggingInitializer initializer = ToolContextUtils.buildComponent(LoggingInitializer.class, DefaultLoggingInitializer::new);

        loggerContext.setName(initializer.buildLogContextName());

        String consoleTerm = System.getProperty(ToolProperties.LOG_CONSOLE_TERM_NAME);

        loggerContext.putProperty(ToolProperties.LOG_CONSOLE_TERM_NAME, ((consoleTerm != null) ? consoleTerm : Boolean.toString((System.console() != null))));
        loggerContext.putProperty(ToolProperties.LOG_DIR_NAME, initializer.buildLogDirectory().getPath());
        loggerContext.putProperty(ToolProperties.LOG_FILE_NAME_NAME, initializer.buildLogFileName());

        URL configFileUrl = initializer.buildLogConfigFileUrl();

        try {
            GafferConfigurator gafferConfigurator = new GafferConfigurator(loggerContext);

            loggerContext.putObject(ClassicConstants.GAFFER_CONFIGURATOR_FQCN, gafferConfigurator);

            gafferConfigurator.run(IOUtils.toString(configFileUrl));
        } catch (IOException e) {
            throw new ApplicationContextException(String.format("Unable to process Logback configuration file (path=%s).", configFileUrl), e);
        }

        StatusManager statusManager = loggerContext.getStatusManager();
        StatusUtil statusUtil = new StatusUtil(statusManager);
        long lastResetTime = statusUtil.timeOfLastReset();

        if (statusUtil.getHighestLevel(lastResetTime) >= Status.WARN) {
            StatusPrinter.print(statusManager, lastResetTime);

            throw new ApplicationContextException(String.format("Unable to initialize Logback using configuration file (path=%s).", configFileUrl));
        }
    }

    @Override
    public void onLevelChange(Logger logger, Level level) {
    }

    @Override
    public void onReset(LoggerContext loggerContext) {
    }

    @Override
    public void onStop(LoggerContext loggerContext) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
    }

    @Override
    public void onStart(LoggerContext loggerContext) {
        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }
}
