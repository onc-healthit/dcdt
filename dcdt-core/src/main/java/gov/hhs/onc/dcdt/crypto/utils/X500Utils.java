package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolStreamUtils;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.stream.Stream;
import javax.annotation.Nullable;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.ASN1ObjectIdentifier;
import org.bouncycastle.asn1.x500.AttributeTypeAndValue;
import org.bouncycastle.asn1.x500.RDN;
import org.bouncycastle.asn1.x500.X500Name;
import org.bouncycastle.asn1.x500.X500NameBuilder;
import org.bouncycastle.asn1.x500.X500NameStyle;
import org.bouncycastle.asn1.x500.style.BCStyle;
import org.bouncycastle.asn1.x500.style.IETFUtils;

public abstract class X500Utils {
    public final static X500NameStyle BC_X500_NAME_STYLE = BCStyle.INSTANCE;

    public static X500Name buildName(Map<ASN1ObjectIdentifier, ASN1Encodable> attrMap) {
        X500NameBuilder builder = new X500NameBuilder(BC_X500_NAME_STYLE);

        attrMap.forEach(builder::addRDN);

        return builder.build();
    }

    public static Map<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(RDN ... rdns) {
        return mapAttributes(ToolArrayUtils.asList(rdns));
    }

    public static Map<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(Collection<RDN> rdns) {
        return rdns.stream().flatMap(rdn -> Stream.of(rdn.getTypesAndValues()))
            .collect(ToolStreamUtils.toMap(AttributeTypeAndValue::getType, AttributeTypeAndValue::getValue, LinkedHashMap::new));
    }

    @Nullable
    public static ASN1Encodable toEncodableValue(ASN1ObjectIdentifier oid, @Nullable String strValue) {
        return (strValue != null) ? BC_X500_NAME_STYLE.stringToValue(oid, strValue) : null;
    }

    @Nullable
    public static String toStringValue(@Nullable ASN1Encodable encodableValue) {
        return (encodableValue != null) ? IETFUtils.valueToString(encodableValue) : null;
    }
}
