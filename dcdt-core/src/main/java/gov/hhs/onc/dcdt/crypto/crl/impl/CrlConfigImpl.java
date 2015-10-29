package gov.hhs.onc.dcdt.crypto.crl.impl;

import gov.hhs.onc.dcdt.crypto.certs.CertificateDn;
import gov.hhs.onc.dcdt.crypto.certs.SignatureAlgorithm;
import gov.hhs.onc.dcdt.crypto.crl.CrlConfig;
import gov.hhs.onc.dcdt.crypto.crl.CrlEntryConfig;
import java.util.stream.Stream;
import javax.annotation.Nullable;

public class CrlConfigImpl extends AbstractCrlDescriptor<CrlEntryConfig> implements CrlConfig {
    @Override
    public void setEntries(@Nullable CrlEntryConfig ... entries) {
        this.entries.clear();

        if (entries != null) {
            // noinspection ConstantConditions
            Stream.of(entries).forEach(entry -> this.entries.put(entry.getSerialNumber(), entry));
        }
    }

    @Override
    public void setIssuerDn(@Nullable CertificateDn issuerDn) {
        this.issuerDn = issuerDn;
    }

    @Override
    public void setSignatureAlgorithm(@Nullable SignatureAlgorithm sigAlg) {
        this.sigAlg = sigAlg;
    }
}
