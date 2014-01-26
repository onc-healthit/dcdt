package gov.hhs.onc.dcdt.crypto.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.CryptographyDescriptor;
import javax.persistence.MappedSuperclass;

@MappedSuperclass
public abstract class AbstractCryptographyDescriptor extends AbstractToolBean implements CryptographyDescriptor {
}
