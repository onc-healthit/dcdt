package gov.hhs.onc.dcdt.crypto.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlType;
import gov.hhs.onc.dcdt.crypto.utils.CryptographyUtils.ToolProviderJcaJceHelper;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.security.cert.CRLException;
import java.security.cert.CertificateException;
import java.security.cert.CertificateFactory;
import java.security.cert.X509CRL;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;
import org.bouncycastle.asn1.x509.CertificateList;
import org.bouncycastle.asn1.x509.TBSCertList.CRLEntry;
import org.bouncycastle.jce.provider.X509CRLEntryObject;
import org.bouncycastle.jce.provider.X509CRLObject;

public final class CrlUtils {
    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static class ToolX509CrlEntry extends X509CRLEntryObject {
        private CRLEntry entry;

        public ToolX509CrlEntry(CRLEntry entry) {
            super(entry);

            this.entry = entry;
        }

        public CRLEntry getEntry() {
            return this.entry;
        }
    }

    @SuppressWarnings({ CompilerWarnings.UNCHECKED })
    public static class ToolX509Crl extends X509CRLObject {
        private CertificateList certList;

        public ToolX509Crl(CertificateList certList) throws CRLException {
            super(certList);

            this.certList = certList;
        }

        public CertificateList getCertificateList() {
            return this.certList;
        }
    }

    private CrlUtils() {
    }

    public static List<ToolX509Crl> readCrls(byte[] data, CrlType crlType) throws CryptographyException {
        return readCrls(CryptographyUtils.PROVIDER_HELPER, data, crlType);
    }

    public static List<ToolX509Crl> readCrls(ToolProviderJcaJceHelper provHelper, byte[] data, CrlType crlType) throws CryptographyException {
        try (InputStream crlsInStream = new ByteArrayInputStream(data)) {
            List<X509CRL> rawCrls =
                ToolStreamUtils.asInstances(getCertificateFactory(provHelper, crlType).generateCRLs(crlsInStream).stream(), X509CRL.class).collect(
                    Collectors.toList());
            List<ToolX509Crl> crls = new ArrayList<>(rawCrls.size());

            for (X509CRL rawCrl : rawCrls) {
                crls.add(readCrl(rawCrl.getEncoded(), crlType, DataEncoding.DER));
            }

            return crls;
        } catch (CRLException | IOException e) {
            throw new CrlException(String.format("Unable to read CRL instance(s) of type (id=%s) from data.", crlType.getId()), e);
        }
    }

    public static ToolX509Crl readCrl(byte[] data, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(ToolEnumUtils.findByType(PemType.class, crlType.getType()), data);
            }

            return new ToolX509Crl(CertificateList.getInstance(data));
        } catch (CRLException e) {
            throw new CrlException(String.format("Unable to read CRL instance of type (id=%s) from data.", crlType.getId()), e);
        }
    }

    public static byte[] writeCrl(ToolX509Crl crl, DataEncoding dataEnc) throws CryptographyException {
        try {
            byte[] data = crl.getEncoded();

            return ((dataEnc == DataEncoding.DER) ? data : PemUtils.writePemContent(PemType.CRL, data));
        } catch (CRLException e) {
            throw new CrlException(String.format("Unable to write CRL instance (class=%s) to data.", ToolClassUtils.getClass(crl)), e);
        }
    }

    public static CertificateFactory getCertificateFactory(CrlType crlType) throws CryptographyException {
        return getCertificateFactory(CryptographyUtils.PROVIDER_HELPER, crlType);
    }

    public static CertificateFactory getCertificateFactory(ToolProviderJcaJceHelper provHelper, CrlType crlType) throws CryptographyException {
        try {
            return provHelper.createCertificateFactory(crlType.getId());
        } catch (CertificateException e) {
            throw new CrlException(String.format("Unable to get certificate factory instance for CRL type (id=%s, providerName=%s).", crlType.getId(),
                provHelper.getProvider().getName()), e);
        }
    }
}
