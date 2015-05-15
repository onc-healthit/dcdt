package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.collections.impl.AbstractToolTransformer;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolIteratorUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.collections4.PredicateUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.codec.api.BinaryAttributeDetector;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;

public abstract class ToolLdapAttributeUtils {
    public static class LdapAttributeIdTransformer extends AbstractToolTransformer<Attribute, String[]> {
        private BinaryAttributeDetector binaryAttrDetector;

        public LdapAttributeIdTransformer(BinaryAttributeDetector binaryAttrDetector) {
            this.binaryAttrDetector = binaryAttrDetector;
        }

        @Override
        protected String[] transformInternal(Attribute attr) throws Exception {
            String attrId = attr.getId();

            return (this.binaryAttrDetector.isBinary(attrId) ? ArrayUtils.toArray(getBinaryAttributeId(attrId), attrId) : ArrayUtils.toArray(attrId));
        }
    }

    public final static String DELIM_ATTR = ":";
    public final static String DELIM_ATTR_BINARY = StringUtils.repeat(DELIM_ATTR, 2);

    public final static String DELIM_ATTR_ID = ";";

    public final static String ATTR_ID_OPT_BINARY = "binary";

    public static String[] buildLookupAttributeIds(BinaryAttributeDetector binaryAttrDetector, @Nullable Set<Attribute> attrs) {
        // noinspection ConstantConditions
        return (!CollectionUtils.isEmpty(attrs) ? ToolCollectionUtils.toArray(CollectionUtils.select(IteratorUtils.asIterable(ToolIteratorUtils
            .chainedArrayIterator(CollectionUtils.collect(attrs, new LdapAttributeIdTransformer(binaryAttrDetector)))), PredicateUtils.uniquePredicate()),
            String.class) : ArrayUtils.EMPTY_STRING_ARRAY);
    }

    public static String getBinaryAttributeId(String attrId) {
        return StringUtils.appendIfMissing(attrId, (DELIM_ATTR_ID + ATTR_ID_OPT_BINARY));
    }

    public static Attribute readAttribute(String str) throws ToolLdapException {
        LdifReader attrLdifReader = new LdifReader();
        LdifEntry attrLdifEntry = new LdifEntry();

        // noinspection ConstantConditions
        for (String strLine : ToolStringUtils.splitLines(str)) {
            try {
                attrLdifReader.parseAttributeValue(attrLdifEntry, strLine, strLine.toLowerCase());
            } catch (LdapException e) {
                throw new ToolLdapException(String.format("Unable to read LDAP attribute from LDIF string line: %s", strLine), e);
            }
        }

        return attrLdifEntry.getEntry().iterator().next();
    }

    public static String writeAttribute(Attribute attr) throws ToolLdapException {
        try {
            return LdifUtils.convertToLdif(attr);
        } catch (LdapException e) {
            throw new ToolLdapException(String.format("Unable to write LDAP attribute (upId={%s}, values=[%s]) to LDIF string.", attr.getUpId(),
                ToolStringUtils.joinDelimit(attr, ", ")), e);
        }
    }
}
