package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithmIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTaggedIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.security.Provider;
import java.security.Security;
import java.util.EnumSet;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x509.AlgorithmIdentifier;
import org.bouncycastle.jcajce.JcaJceHelper;
import org.bouncycastle.jcajce.ProviderJcaJceHelper;
import org.bouncycastle.jce.provider.BouncyCastleProvider;

public abstract class CryptographyUtils {
    public final static Provider PROVIDER = new BouncyCastleProvider();

    public final static String PROVIDER_NAME = PROVIDER.getName();

    public final static JcaJceHelper PROVIDER_HELPER = new ProviderJcaJceHelper(PROVIDER);

    static {
        initializeProvider();
    }

    public static <T extends Enum<T> & CryptographyAlgorithmIdentifier> T findAlgorithmId(Class<T> algEnumClass, AlgorithmIdentifier algId) {
        for (T algEnum : EnumSet.allOf(algEnumClass)) {
            if (Objects.equals(algEnum.getId(), algId)) {
                return algEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyObjectIdentifier> T findObjectId(Class<T> idEnumClass, ASN1ObjectIdentifier oid) {
        for (T idEnum : EnumSet.allOf(idEnumClass)) {
            if (Objects.equals(idEnum.getOid(), oid)) {
                return idEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyTaggedIdentifier> T findTaggedId(Class<T> idEnumClass, int tag) {
        for (T idEnum : EnumSet.allOf(idEnumClass)) {
            if (Objects.equals(idEnum.getTag(), tag)) {
                return idEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <U extends Enum<U> & CryptographyTypeIdentifier> U findTypeId(Class<U> idEnumClass, Class<?> type) {
        for (U idEnum : EnumSet.allOf(idEnumClass)) {
            if (ToolClassUtils.isAssignable(idEnum.getType(), type)) {
                return idEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyIdentifier> T findId(Class<T> idEnumClass, String id) {
        for (T idEnum : EnumSet.allOf(idEnumClass)) {
            if (StringUtils.equalsIgnoreCase(idEnum.getId(), id)) {
                return idEnum;
            }
        }

        return null;
    }

    public static void initializeProvider() {
        Security.removeProvider(PROVIDER_NAME);
        Security.addProvider(PROVIDER);
    }
}
