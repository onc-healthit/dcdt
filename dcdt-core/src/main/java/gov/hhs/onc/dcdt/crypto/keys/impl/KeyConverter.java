package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.Convert;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyType;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.Key;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("convKey")
@Converts({ @Convert(from = byte[].class, to = Key.class), @Convert(from = Key.class, to = byte[].class) })
public class KeyConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_KEY = TypeDescriptor.valueOf(Key.class);

    @Override
    protected Object convertInternal(Object src, TypeDescriptor srcType, TypeDescriptor targetType, ConvertiblePair convPair) throws Exception {
        return (srcType.isAssignableTo(TYPE_DESC_KEY)) ? KeyUtils.writeKey(((Key) src), DataEncoding.PEM) : KeyUtils.readKey(
            ToolEnumUtils.findByType(KeyType.class, srcType.getObjectType()), ((byte[]) src), KeyAlgorithm.RSA, DataEncoding.PEM);
    }
}
