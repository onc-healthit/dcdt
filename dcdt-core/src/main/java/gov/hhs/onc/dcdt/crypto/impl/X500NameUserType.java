package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.data.types.impl.AbstractStringUserType;
import org.bouncycastle.asn1.x500.X500Name;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("x500NameUserType")
@Scope("singleton")
public class X500NameUserType extends AbstractStringUserType<X500Name> {
    public X500NameUserType() {
        super(X500Name.class);
    }
}
