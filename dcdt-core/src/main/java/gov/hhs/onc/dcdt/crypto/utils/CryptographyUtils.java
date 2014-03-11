package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyAlgorithm;
import gov.hhs.onc.dcdt.crypto.CryptographyObjectIdentifier;
import gov.hhs.onc.dcdt.crypto.CryptographyTypeIdentifier;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.security.Provider;
import java.security.Security;
import java.util.EnumSet;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.codec.binary.Base64;
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
        Security.addProvider(PROVIDER);
    }

    public static <T extends Enum<T> & CryptographyAlgorithm> T findAlgorithm(Class<T> algEnumClass, AlgorithmIdentifier algId) {
        for (T algEnum : EnumSet.allOf(algEnumClass)) {
            if (Objects.equals(algEnum.getId(), algId)) {
                return algEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyObjectIdentifier> T findObjectId(Class<T> idEnumClass, ASN1ObjectIdentifier idOid) {
        for (T idEnum : EnumSet.allOf(idEnumClass)) {
            if (Objects.equals(idEnum.getOid(), idOid)) {
                return idEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <U extends Enum<U> & CryptographyTypeIdentifier> U findTypeId(Class<U> idEnumClass, Class<?> idType) {
        for (U idEnum : EnumSet.allOf(idEnumClass)) {
            if (ToolClassUtils.isAssignable(idEnum.getType(), idType)) {
                return idEnum;
            }
        }

        return null;
    }

    @Nullable
    public static <T extends Enum<T> & CryptographyObjectIdentifier> T findId(Class<T> idEnumClass, String idName) {
        for (T idEnum : EnumSet.allOf(idEnumClass)) {
            if (StringUtils.equalsIgnoreCase(idEnum.getName(), idName)) {
                return idEnum;
            }
        }

        return null;
    }

    public static byte[] getDerEncodedData(String data) {
        return Base64.decodeBase64(data);
    }
}
