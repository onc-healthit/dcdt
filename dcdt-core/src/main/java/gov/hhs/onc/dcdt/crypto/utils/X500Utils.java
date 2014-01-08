package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.list.SetUniqueList;
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
    public static class OrderedOidComparator implements Comparator<ASN1ObjectIdentifier> {
        private List<ASN1ObjectIdentifier> orderedOids;

        public OrderedOidComparator(List<ASN1ObjectIdentifier> orderedOids) {
            this.orderedOids = SetUniqueList.setUniqueList(orderedOids);
        }

        @Override
        public int compare(ASN1ObjectIdentifier oid1, ASN1ObjectIdentifier oid2) {
            return Integer.compare(this.orderedOids.indexOf(oid1), this.orderedOids.indexOf(oid2));
        }
    }

    public final static X500NameStyle BC_X500_NAME_STYLE = BCStyle.INSTANCE;

    public static X500Name buildName(Map<ASN1ObjectIdentifier, ASN1Encodable> attrMap) {
        X500NameBuilder x500NameBuilder = new X500NameBuilder(BC_X500_NAME_STYLE);

        for (ASN1ObjectIdentifier attrOid : attrMap.keySet()) {
            x500NameBuilder.addRDN(attrOid, attrMap.get(attrOid));
        }

        return x500NameBuilder.build();
    }

    public static Map<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(RDN... rdns) {
        return mapAttributes(ToolArrayUtils.asList(rdns));
    }

    public static Map<ASN1ObjectIdentifier, ASN1Encodable> mapAttributes(Iterable<RDN> rdns) {
        Map<ASN1ObjectIdentifier, ASN1Encodable> attrMap = new LinkedHashMap<>();

        for (RDN rdn : rdns) {
            for (AttributeTypeAndValue attr : rdn.getTypesAndValues()) {
                attrMap.put(attr.getType(), attr.getValue());
            }
        }

        return attrMap;
    }

    @Nullable
    public static ASN1Encodable toEncodableValue(ASN1ObjectIdentifier oid, @Nullable String strValue) {
        return (strValue != null) ? BC_X500_NAME_STYLE.stringToValue(oid, strValue) : null;
    }

    @Nullable
    public static String toStringValue(ASN1ObjectIdentifier oid, @Nullable ASN1Encodable encodableValue) {
        return (encodableValue != null) ? IETFUtils.valueToString(encodableValue) : null;
    }
}
