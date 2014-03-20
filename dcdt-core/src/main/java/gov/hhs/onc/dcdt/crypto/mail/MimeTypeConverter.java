package gov.hhs.onc.dcdt.crypto.mail;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import javax.annotation.Nullable;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;
import org.springframework.util.MimeType;
import org.springframework.util.MimeTypeUtils;

@Component("mimeTypeConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = MimeType.class) }, serialize = { @Converts(from = MimeType.class, to = String.class) })
@List({ @Converts(from = String.class, to = MimeType.class), @Converts(from = MimeType.class, to = String.class) })
public class MimeTypeConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_MIME_TYPE = TypeDescriptor.valueOf(MimeType.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return sourceType.isAssignableTo(TYPE_DESC_MIME_TYPE) ? source.toString() : MimeTypeUtils.parseMimeType((String) source);
    }
}
