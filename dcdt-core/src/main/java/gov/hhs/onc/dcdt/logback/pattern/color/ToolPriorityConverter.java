package gov.hhs.onc.dcdt.logback.pattern.color;

import static ch.qos.logback.core.pattern.color.ANSIConstants.BLUE_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.BOLD;
import static ch.qos.logback.core.pattern.color.ANSIConstants.DEFAULT_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.RED_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.WHITE_FG;
import static ch.qos.logback.core.pattern.color.ANSIConstants.YELLOW_FG;

import ch.qos.logback.classic.Level;
import ch.qos.logback.classic.pattern.color.HighlightingCompositeConverter;
import ch.qos.logback.classic.spi.ILoggingEvent;
import java.util.HashMap;
import java.util.Map;

public class ToolPriorityConverter extends HighlightingCompositeConverter {
    private static Map<Level, String> fgColorCodeMap = new HashMap<>();

    static {
        fgColorCodeMap.put(Level.TRACE, BOLD + DEFAULT_FG);
        fgColorCodeMap.put(Level.DEBUG, BOLD + WHITE_FG);
        fgColorCodeMap.put(Level.INFO, BOLD + BLUE_FG);
        fgColorCodeMap.put(Level.WARN, BOLD + YELLOW_FG);
        fgColorCodeMap.put(Level.ERROR, BOLD + RED_FG);
    }

    @Override
    protected String getForegroundColorCode(ILoggingEvent event) {
        return fgColorCodeMap.get(event.getLevel());
    }
}
