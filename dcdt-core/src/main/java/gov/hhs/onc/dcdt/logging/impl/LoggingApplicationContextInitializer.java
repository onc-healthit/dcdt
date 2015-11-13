package gov.hhs.onc.dcdt.logging.impl;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.Logger;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferConfigurator;
import ch.qos.logback.classic.jul.LevelChangePropagator;
import ch.qos.logback.classic.spi.LoggerContextListener;
import ch.qos.logback.classic.util.ContextSelectorStaticBinder;
import ch.qos.logback.core.status.Status;
import ch.qos.logback.core.status.StatusManager;
import ch.qos.logback.core.status.StatusUtil;
import ch.qos.logback.core.util.StatusPrinter;
import gov.hhs.onc.dcdt.context.ToolApplicationContextException;
import gov.hhs.onc.dcdt.context.ToolProperties;
import gov.hhs.onc.dcdt.context.impl.AbstractToolApplicationContextInitializer;
import gov.hhs.onc.dcdt.context.utils.ToolContextUtils;
import gov.hhs.onc.dcdt.logging.LoggingInitializer;
import java.io.IOException;
import java.net.URL;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.bridge.SLF4JBridgeHandler;
import org.springframework.context.support.AbstractRefreshableConfigApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;

@Order(Ordered.HIGHEST_PRECEDENCE + 1)
public class LoggingApplicationContextInitializer extends AbstractToolApplicationContextInitializer implements LoggerContextListener {
    private static class DefaultLoggingInitializer extends AbstractLoggingInitializer {
        public DefaultLoggingInitializer(AbstractRefreshableConfigApplicationContext appContext) {
            super(appContext);
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
    }

    @Override
    public boolean isResetResistant() {
        return false;
    }

    @Override
    protected void initializeInternal(AbstractRefreshableConfigApplicationContext appContext) throws Exception {
        LoggerContext loggerContext = ContextSelectorStaticBinder.getSingleton().getContextSelector().getDefaultLoggerContext();

        if (ObjectUtils.defaultIfNull(((Boolean) loggerContext.getObject(ToolProperties.LOG_CONTEXT_INITIALIZED_NAME)), Boolean.FALSE)) {
            loggerContext.getLogger(LoggingApplicationContextInitializer.class).info("Skipping logging initialization.");

            return;
        }

        SLF4JBridgeHandler.removeHandlersForRootLogger();
        SLF4JBridgeHandler.install();

        loggerContext.stop();
        loggerContext.reset();

        loggerContext.addListener(this);

        LevelChangePropagator lvlChangePropagator = new LevelChangePropagator();
        lvlChangePropagator.setContext(loggerContext);
        lvlChangePropagator.setResetJUL(true);
        loggerContext.addListener(lvlChangePropagator);

        LoggingInitializer loggingInit = ToolContextUtils.buildComponent(LoggingInitializer.class, () -> new DefaultLoggingInitializer(appContext), appContext);

        loggerContext.setName(loggingInit.buildLogContextName());

        loggerContext.putProperty(ToolProperties.LOG_CONSOLE_TERM_NAME, Boolean.toString(loggingInit.buildLogConsoleTerminal()));
        loggerContext.putProperty(ToolProperties.LOG_DIR_NAME, loggingInit.buildLogDirectory().getPath());
        loggerContext.putProperty(ToolProperties.LOG_FILE_NAME_NAME, loggingInit.buildLogFileName());

        URL configFileUrl = loggingInit.buildLogConfigFileUrl();

        try {
            GafferConfigurator gafferConfigurator = new GafferConfigurator(loggerContext);

            loggerContext.putObject(ClassicConstants.GAFFER_CONFIGURATOR_FQCN, gafferConfigurator);

            gafferConfigurator.run(IOUtils.toString(configFileUrl));
        } catch (IOException e) {
            throw new ToolApplicationContextException(String.format("Unable to process Logback configuration file (path=%s).", configFileUrl), e);
        }

        StatusManager statusManager = loggerContext.getStatusManager();
        StatusUtil statusUtil = new StatusUtil(statusManager);
        long lastResetTime = statusUtil.timeOfLastReset();

        if (statusUtil.getHighestLevel(lastResetTime) > Status.WARN) {
            StatusPrinter.print(statusManager, lastResetTime);

            throw new ToolApplicationContextException(String.format("Unable to initialize Logback using configuration file (path=%s).", configFileUrl));
        }

        loggerContext.putObject(ToolProperties.LOG_CONTEXT_INITIALIZED_NAME, Boolean.TRUE);

        loggerContext.getLogger(LoggingApplicationContextInitializer.class).info(
            String.format("Logging initialized (initializerClass=%s, configFileUrl=%s).", loggingInit.getClass().getName(), configFileUrl.toString()));
    }
}
