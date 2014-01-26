package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.convert.Converts;
import gov.hhs.onc.dcdt.convert.Converts.List;
import gov.hhs.onc.dcdt.convert.impl.AbstractToolConverter;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.keys.KeyAlgorithm;
import gov.hhs.onc.dcdt.crypto.keys.KeyType;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils;
import gov.hhs.onc.dcdt.crypto.utils.KeyUtils;
import java.security.Key;
import javax.annotation.Nullable;
import org.springframework.context.annotation.Scope;
import org.springframework.core.convert.TypeDescriptor;
import org.springframework.stereotype.Component;

@Component("keyConv")
@List({ @Converts(from = byte[].class, to = Key.class), @Converts(from = Key.class, to = byte[].class) })
@Scope("singleton")
public class KeyConverter extends AbstractToolConverter {
    private final static TypeDescriptor TYPE_DESC_KEY = TypeDescriptor.valueOf(Key.class);

    @Nullable
    @Override
    protected Object convertInternal(Object source, TypeDescriptor sourceType, TypeDescriptor targetType, ConvertiblePair convertPair) throws Exception {
        return (sourceType.isAssignableTo(TYPE_DESC_KEY)) ? KeyUtils.writeKey((Key) source, DataEncoding.PEM) : KeyUtils.readKey(
            CryptographyUtils.findTypeId(KeyType.class, sourceType.getObjectType()), (byte[]) source, KeyAlgorithm.RSA, DataEncoding.PEM);
    }
}
