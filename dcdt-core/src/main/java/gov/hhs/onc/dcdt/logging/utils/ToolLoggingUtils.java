package gov.hhs.onc.dcdt.logging.utils;

import ch.qos.logback.classic.ClassicConstants;
import ch.qos.logback.classic.LoggerContext;
import ch.qos.logback.classic.gaffer.GafferConfigurator;
import java.io.FileNotFoundException;
import java.io.IOException;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.springframework.util.ResourceUtils;

public final class ToolLoggingUtils {
    private ToolLoggingUtils() {
    }

    public static void include(LoggerContext context, String ... locs) throws IOException {
        StrBuilder contentBuilder = new StrBuilder();
        contentBuilder.setNewLineText(StringUtils.LF);

        for (String loc : locs) {
            try {
                contentBuilder.append(IOUtils.toString(ResourceUtils.getURL((ResourceUtils.CLASSPATH_URL_PREFIX + loc))));

                contentBuilder.appendNewLine();
            } catch (FileNotFoundException ignored) {
            }
        }

        ((GafferConfigurator) context.getObject(ClassicConstants.GAFFER_CONFIGURATOR_FQCN)).run(contentBuilder.build());
    }
}
