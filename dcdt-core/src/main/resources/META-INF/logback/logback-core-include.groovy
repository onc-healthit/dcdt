import ch.qos.logback.classic.encoder.PatternLayoutEncoder
import ch.qos.logback.core.ConsoleAppender
import ch.qos.logback.core.rolling.RollingFileAppender
import ch.qos.logback.core.rolling.SizeAndTimeBasedFNATP
import ch.qos.logback.core.rolling.TimeBasedRollingPolicy
import ch.qos.logback.core.status.NopStatusListener
import gov.hhs.onc.dcdt.context.ToolProperties
import gov.hhs.onc.dcdt.context.impl.ToolMessageSourceImpl
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter
import gov.hhs.onc.dcdt.data.events.impl.LoggingBeanEntityInterceptor
import gov.hhs.onc.dcdt.json.impl.ToolObjectMapper
import gov.hhs.onc.dcdt.logging.impl.PriorityColorCompositeConverter
import gov.hhs.onc.dcdt.logging.impl.RootCauseThrowableProxyConverter
import gov.hhs.onc.dcdt.velocity.impl.ToolVelocityEngineFactoryBean
import org.apache.commons.io.FilenameUtils
import org.apache.commons.lang3.ObjectUtils
import org.springframework.beans.factory.support.DefaultListableBeanFactory

/*====================================================================================================
= PROPERTIES: CONSOLE
=====================================================================================================*/
def consolePattern = ObjectUtils.defaultIfNull(context.getProperty(ToolProperties.LOG_CONSOLE_PATTERN_NAME), "%pColor - %m%n%exRoot")

def consoleTarget = ObjectUtils.defaultIfNull(context.getProperty(ToolProperties.LOG_CONSOLE_TARGET_NAME), "System.out")

/*====================================================================================================
= PROPERTIES: FILE
=====================================================================================================*/
def fileDir = context.getProperty(ToolProperties.LOG_DIR_NAME)

def fileName = context.getProperty(ToolProperties.LOG_FILE_NAME_NAME)

def fileExt = ObjectUtils.defaultIfNull(context.getProperty(ToolProperties.LOG_FILE_EXT_NAME), "${FilenameUtils.EXTENSION_SEPARATOR}log")

def filePattern = ObjectUtils.defaultIfNull(context.getProperty(ToolProperties.LOG_FILE_PATTERN_NAME),
    "%d{yyyy-MM-dd HH:mm:ss z} [%C:%L %t] %p - %m%n%exRoot")

def fileSizeMax = ObjectUtils.defaultIfNull(context.getProperty(ToolProperties.LOG_FILE_SIZE_MAX_NAME), "50MB")

/*====================================================================================================
= CONVERSION RULES
=====================================================================================================*/
conversionRule("exRoot", RootCauseThrowableProxyConverter)

conversionRule("pColor", PriorityColorCompositeConverter)

/*====================================================================================================
= STATUS LISTENERS
=====================================================================================================*/
statusListener(NopStatusListener)

/*====================================================================================================
= APPENDER: CONSOLE
=====================================================================================================*/
appender("console", ConsoleAppender) {
    encoder(PatternLayoutEncoder) {
        pattern = consolePattern
    }
    target = consoleTarget
    withJansi = true
}

/*====================================================================================================
= APPENDER: FILE
=====================================================================================================*/
appender("file", RollingFileAppender) {
    file = "${fileDir}/${fileName}${fileExt}"
    rollingPolicy(TimeBasedRollingPolicy) {
        fileNamePattern = "${fileDir}/${fileName}.%d{yyyy-MM-dd}.%i${fileExt}"
        timeBasedFileNamingAndTriggeringPolicy(SizeAndTimeBasedFNATP) {
            maxFileSize = fileSizeMax
        }
    }
    encoder(PatternLayoutEncoder) {
        pattern = filePattern
    }
}

/*====================================================================================================
= LOGGERS: PROJECT
=====================================================================================================*/
logger("gov.hhs.onc.dcdt", ALL, [ "console", "file" ], false)

logger(ToolMessageSourceImpl.name, INFO, [ "console", "file" ], false)

logger(AbstractToolConverter.name, DEBUG, [ "console", "file" ], false)

logger(LoggingBeanEntityInterceptor.name, DEBUG, [ "console", "file" ], false)

logger(ToolObjectMapper.name, INFO, [ "console", "file" ], false)

logger(ToolVelocityEngineFactoryBean.name, INFO, [ "console", "file" ], false)

/*====================================================================================================
= LOGGERS: APACHE
=====================================================================================================*/
logger("org.apache", INFO, [ "console", "file" ], false)

/*====================================================================================================
= LOGGERS: HIBERNATE
=====================================================================================================*/
logger("org.hibernate", INFO, [ "console", "file" ], false)

// logger("org.hibernate.SQL", DEBUG, [ "console", "file" ], false)

// logger("org.hibernate.type", TRACE, [ "console", "file" ], false)

/*====================================================================================================
= LOGGERS: SPRING FRAMEWORK
=====================================================================================================*/
logger("org.springframework", INFO, [ "console", "file" ], false)

logger(DefaultListableBeanFactory.name, WARN, [ "console", "file" ], false)

logger("org.springframework.context.support.PostProcessorRegistrationDelegate\$BeanPostProcessorChecker", WARN, [ "console", "file" ], false)

/*====================================================================================================
= ROOT LOGGER
=====================================================================================================*/
root(WARN, [ "console", "file" ])
