package gov.hhs.onc.dcdt.io.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Locale;
import org.springframework.stereotype.Component;

@Component("formatterPath")
public class PathFormatter extends AbstractToolFormatter<Path> {
    public PathFormatter() {
        super(Path.class);
    }

    @Override
    protected Path parseInternal(String str, Locale locale) throws Exception {
        return Paths.get(str);
    }
}
