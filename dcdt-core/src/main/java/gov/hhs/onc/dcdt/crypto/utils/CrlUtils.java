package gov.hhs.onc.dcdt.crypto.utils;

import com.github.sebhoss.warnings.CompilerWarnings;
import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.DataEncoding;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.crypto.crl.CrlException;
import gov.hhs.onc.dcdt.crypto.crl.CrlType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.security.cert.CRLException;
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

    public static ToolX509Crl readCrl(byte[] data, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(CryptographyUtils.findByType(PemType.class, crlType.getType()), data);
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

    // @formatter:off
    /*
    public final static JcaX509CRLConverter CRL_CONV = new JcaX509CRLConverter() {
        {
            this.setProvider(CryptographyUtils.PROVIDER);
        }
    };

    private CrlUtils() {
    }

    public static X509CRL readCrl(InputStream inStream, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        return readCrl(CryptographyUtils.PROVIDER_HELPER, inStream, crlType, dataEnc);
    }

    public static X509CRL readCrl(ToolProviderJcaJceHelper provHelper, InputStream inStream, CrlType crlType, DataEncoding dataEnc)
        throws CryptographyException {
        return readCrl(provHelper, new InputStreamReader(inStream), crlType, dataEnc);
    }

    public static X509CRL readCrl(Reader reader, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        return readCrl(CryptographyUtils.PROVIDER_HELPER, reader, crlType, dataEnc);
    }

    public static X509CRL readCrl(ToolProviderJcaJceHelper provHelper, Reader reader, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        try {
            return readCrl(provHelper, IOUtils.toByteArray(reader), crlType, dataEnc);
        } catch (IOException e) {
            throw new CrlException(String.format("Unable to read CRL instance of type (id=%s, providerName=%s) from reader (class=%s).", crlType.getId(),
                provHelper.getProvider().getName(), ToolClassUtils.getName(reader)), e);
        }
    }

    public static X509CRL readCrl(byte[] data, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        return readCrl(CryptographyUtils.PROVIDER_HELPER, data, crlType, dataEnc);
    }

    public static X509CRL readCrl(ToolProviderJcaJceHelper provHelper, byte[] data, CrlType crlType, DataEncoding dataEnc) throws CryptographyException {
        try {
            if (dataEnc == DataEncoding.PEM) {
                data = PemUtils.writePemContent(CryptographyUtils.findByType(PemType.class, crlType.getType()), data);
            }

            return ((X509CRL) getCertificateFactory(provHelper, crlType).generateCRL(new ByteArrayInputStream(data)));
        } catch (CRLException e) {
            throw new CrlException(String.format("Unable to read CRL instance of type (id=%s, providerName=%s) from data.", crlType.getId(), provHelper
                .getProvider().getName()), e);
        }
    }

    public static void writeCrl(OutputStream outStream, X509CRL crl, DataEncoding dataEnc) throws CryptographyException {
        writeCrl(new OutputStreamWriter(outStream), crl, dataEnc);
    }

    public static void writeCrl(Writer writer, X509CRL crl, DataEncoding dataEnc) throws CryptographyException {
        try {
            IOUtils.write(writeCrl(crl, dataEnc), writer);

            writer.flush();
        } catch (IOException e) {
            throw new CrlException(String.format("Unable to write CRL instance (class=%s) to writer (class=%s).", ToolClassUtils.getClass(crl),
                ToolClassUtils.getName(writer)), e);
        }
    }

    public static byte[] writeCrl(X509CRL crl, DataEncoding dataEnc) throws CryptographyException {
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
            throw new CrlException(String.format(
                "Unable to get certificate factory instance for Certificate Revocation List (CRL) type (id=%s, providerName=%s).", crlType.getId(), provHelper
                    .getProvider().getName()), e);
        }
    }
    */
    // @formatter:on
}
