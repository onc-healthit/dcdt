package gov.hhs.onc.dcdt.io.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.ConvertsJson;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Objects;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("pathConv")
@ConvertsJson(deserialize = { @Converts(from = String.class, to = Path.class) }, serialize = { @Converts(from = Path.class, to = String.class) })
@List({ @Converts(from = String[].class, to = Path.class), @Converts(from = String.class, to = Path.class), @Converts(from = File.class, to = Path.class),
    @Converts(from = Path.class, to = File.class), @Converts(from = Path.class, to = String.class) })
@Scope("singleton")
public class PathConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_FILE = TypeDescriptor.valueOf(File.class);
    private final static TypeDescriptor TYPE_DESC_PATH = TypeDescriptor.valueOf(Path.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        if (sourceType.isAssignableTo(TYPE_DESC_PATH)) {
            return targetType.isAssignableTo(TYPE_DESC_STR) ? Objects.toString(source) : ((Path) source).toFile();
        } else if (sourceType.isAssignableTo(TYPE_DESC_FILE)) {
            return ((File) source).toPath();
        } else {
            return Paths.get((String) source);
        }
    }
}
