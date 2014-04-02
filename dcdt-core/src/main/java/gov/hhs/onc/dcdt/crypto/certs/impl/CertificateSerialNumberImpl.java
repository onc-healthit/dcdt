package gov.hhs.onc.dcdt.crypto.certs.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.crypto.certs.CertificateSerialNumber;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.math.BigInteger;
import javax.annotation.Nullable;
import org.apache.commons.codec.binary.Hex;

public class CertificateSerialNumberImpl extends AbstractToolBean implements CertificateSerialNumber {
    private BigInteger value;

    public CertificateSerialNumberImpl(BigInteger value) {
        this.value = value;
    }

    @Override
    public String toString() {
        return Hex.encodeHexString(this.value.toByteArray());
    }

    @Override
    @SuppressWarnings({ "EqualsWhichDoesntCheckParameterClass" })
    public boolean equals(@Nullable Object obj) {
        // noinspection ConstantConditions
        return (ToolClassUtils.isAssignable(ToolClassUtils.getClass(obj), CertificateSerialNumber.class) && ((CertificateSerialNumber) obj).getValue().equals(
            this.value));
    }

    @Override
    public int hashCode() {
        return this.value.hashCode();
    }

    @Override
    public BigInteger getValue() {
        return this.value;
    }
}
