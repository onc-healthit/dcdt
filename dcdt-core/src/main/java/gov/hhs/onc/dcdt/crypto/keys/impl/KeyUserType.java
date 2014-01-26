package gov.hhs.onc.dcdt.crypto.keys.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractBlobUserType;
import java.security.Key;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("keyUserType")
@Scope("singleton")
public class KeyUserType extends AbstractBlobUserType<Key> {
    public KeyUserType() {
        super(Key.class);
    }
}
