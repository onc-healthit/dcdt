package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractBlobUserType;
import java.security.Key;
import org.springframework.stereotype.Component;

@Component("keyUserType")
public class KeyUserType extends AbstractBlobUserType<Key> {
    public KeyUserType() {
        super(Key.class);
    }
}
