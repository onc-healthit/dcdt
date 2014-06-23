package gov.hhs.onc.dcdt.net.mime.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import java.util.Locale;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Component("formatterMimeType")
public class MimeTypeFormatter extends AbstractToolFormatter<MimeType> {
    public MimeTypeFormatter() {
        super(MimeType.class);
    }

    @Override
    protected MimeType parseInternal(String str, Locale locale) throws Exception {
        return MimeTypeUtils.parseMimeType(str);
    }
}
