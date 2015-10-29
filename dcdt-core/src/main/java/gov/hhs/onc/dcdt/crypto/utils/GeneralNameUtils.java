package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.GeneralNameType;
import gov.hhs.onc.dcdt.utils.ToolEnumUtils;
import gov.hhs.onc.dcdt.utils.ToolMapUtils.ToolMultiValueMap;
import java.util.LinkedHashMap;
import java.util.stream.Stream;
import org.bouncycastle.asn1.ASN1Encodable;
import org.bouncycastle.asn1.x509.GeneralName;
import org.bouncycastle.asn1.x509.GeneralNames;

public final class GeneralNameUtils {
    private GeneralNameUtils() {
    }

    public static GeneralNames fromMap(ToolMultiValueMap<GeneralNameType, ASN1Encodable> nameMap) {
        return new GeneralNames(nameMap
            .keySet()
            .stream()
            .flatMap(
                nameType -> (!nameMap.isEmpty(nameType)
                    ? nameMap.getCollection(nameType).stream().map(name -> new GeneralName(nameType.getTag(), name)) : Stream.empty()))
            .toArray(GeneralName[]::new));
    }

    public static ToolMultiValueMap<GeneralNameType, ASN1Encodable> toMap(GeneralNames names) {
        return toMap(names.getNames());
    }

    public static ToolMultiValueMap<GeneralNameType, ASN1Encodable> toMap(GeneralName ... names) {
        ToolMultiValueMap<GeneralNameType, ASN1Encodable> nameMap = new ToolMultiValueMap<>(new LinkedHashMap<>(names.length));

        Stream.of(names).forEach(nameItem -> {
            int nameTag = nameItem.getTagNo();

            nameMap.put(ToolEnumUtils.findByPredicate(GeneralNameType.class, enumItem -> (enumItem.getTag() == nameTag)), nameItem.getName());
        });

        return nameMap;
    }
}
