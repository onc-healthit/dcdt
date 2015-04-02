package gov.hhs.onc.dcdt.io.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convPath")
@Converts({ @Convert(from = String[].class, to = Path.class), @Convert(from = File.class, to = Path.class), @Convert(from = Path.class, to = File.class) })
public class PathConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_FILE = TypeDescriptor.valueOf(File.class);
    private final static TypeDescriptor TYPE_DESC_PATH = TypeDescriptor.valueOf(Path.class);

    @Nullable
    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        if (srcType.isAssignableTo(TYPE_DESC_PATH)) {
            return ((Path) src).toFile();
        } else if (srcType.isAssignableTo(TYPE_DESC_FILE)) {
            return ((File) src).toPath();
        } else {
            String[] srcStrs = ((String[]) src);

            return (!ArrayUtils.isEmpty(srcStrs) ? Paths.get(srcStrs[0], ToolArrayUtils.slice(srcStrs, 1)) : null);
        }
    }
}
