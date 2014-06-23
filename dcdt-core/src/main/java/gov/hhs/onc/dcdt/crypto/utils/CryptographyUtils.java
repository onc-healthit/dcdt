package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import java.security.Provider;
import java.security.Security;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.ProviderJcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class CryptographyUtils {
    public static class ToolProviderJcaJceHelper extends ProviderJcaJceHelper {
        public ToolProviderJcaJceHelper(Provider prov) {
            super(prov);
        }

        public Provider getProvider() {
            return this.provider;
        }
    }

    public final static Provider PROVIDER = new BouncyCastleProvider();
    public final static String PROVIDER_NAME = PROVIDER.getName();
    public final static ToolProviderJcaJceHelper PROVIDER_HELPER = new ToolProviderJcaJceHelper(PROVIDER);

    public final static Provider JCE_PROVIDER = Security.getProvider("SUN");
    public final static String JCE_PROVIDER_NAME = JCE_PROVIDER.getName();
    public final static ToolProviderJcaJceHelper JCE_PROVIDER_HELPER = new ToolProviderJcaJceHelper(JCE_PROVIDER);

    static {
        initializeProvider();
    }

    public static <T extends Enum<T> & CryptographyAlgorithmIdentifier> T findByAlgorithmId(Class<T> enumClass, AlgorithmIdentifier algId) {
        return ToolEnumUtils.findByPropertyValue(enumClass, CryptographyAlgorithmIdentifier.PROP_NAME_ALG_ID, algId);
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyObjectIdentifier> T findByOid(Class<T> enumClass, ASN1ObjectIdentifier oid) {
        return ToolEnumUtils.findByPropertyValue(enumClass, CryptographyObjectIdentifier.PROP_NAME_OID, oid);
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyTaggedIdentifier> T findByTag(Class<T> enumClass, int tag) {
        return ToolEnumUtils.findByPropertyValue(enumClass, CryptographyTaggedIdentifier.PROP_NAME_TAG, tag);
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyTypeIdentifier> T findByType(Class<T> enumClass, Class<?> type) {
        return ToolEnumUtils.findByPropertyValue(enumClass, CryptographyTypeIdentifier.PROP_NAME_TYPE, type);
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyIdentifier> T findById(Class<T> enumClass, String id) {
        return ToolEnumUtils.findByPropertyValue(enumClass, CryptographyIdentifier.PROP_NAME_ID, id);
    }

    public static void initializeProvider() {
        Security.removeProvider(PROVIDER_NAME);
        Security.addProvider(PROVIDER);
    }
}
