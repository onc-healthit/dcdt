package gov.hhs.onc.dcdt.logging.impl;

public class LoggingConfigurator /*extends LoggerContextAwareBase implements Configurator, LoggerContextListener*/ {
    // @formatter:off
    /*
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

        ((SubstituteLoggerFactory) LoggerFactory.getILoggerFactory()).clear();
        
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
    */
    // @formatter:on
}
