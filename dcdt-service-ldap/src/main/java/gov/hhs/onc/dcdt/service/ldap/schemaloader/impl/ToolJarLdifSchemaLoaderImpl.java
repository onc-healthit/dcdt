package gov.hhs.onc.dcdt.service.ldap.schemaloader.impl;

import gov.hhs.onc.dcdt.ldap.utils.ToolLdifUtils;
import gov.hhs.onc.dcdt.ldap.utils.ToolRdnUtils;
import gov.hhs.onc.dcdt.service.ldap.schemaloader.ToolJarLdifSchemaLoader;
import gov.hhs.onc.dcdt.utils.ToolResourceUtils;
import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.api.ldap.model.schema.registries.AbstractSchemaLoader;
import org.apache.directory.api.ldap.model.schema.registries.Schema;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.core.io.support.ResourcePatternResolver;
import org.springframework.stereotype.Component;

@Component("toolJarLdifSchemaLoaderImpl")
public class ToolJarLdifSchemaLoaderImpl extends AbstractSchemaLoader implements ToolJarLdifSchemaLoader {
    private final static String DELIM_RESOURCE_PATTERN = "/";

    private final static String RESOURCE_PATTERN_PREFIX_SCHEMA = ToolResourceUtils.CLASSPATH_ALL_URL_PREFIX
        + StringUtils.join(
            ArrayUtils.toArray("schema", (SchemaConstants.OU_AT + ToolRdnUtils.DELIM_RDN + "schema"), (SchemaConstants.CN_AT + ToolRdnUtils.DELIM_RDN)),
            DELIM_RESOURCE_PATTERN);

    private final static String RESOURCE_PATTERN_SUFFIX_LDIF_FILE = ".ldif";
    private final static String RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE = DELIM_RESOURCE_PATTERN + "m-oid=*" + RESOURCE_PATTERN_SUFFIX_LDIF_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_ATTR_TYPE_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.ATTRIBUTE_TYPES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_COMPARATOR_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.COMPARATORS_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_DIT_CONTENT_RULE_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.DIT_CONTENT_RULES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_DIT_STRUCT_RULE_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.DIT_STRUCTURE_RULES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_MATCHING_RULE_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.MATCHING_RULES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_MATCHING_RULE_USE_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.MATCHING_RULE_USE_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_NAME_FORM_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.NAME_FORMS_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_NORMALIZER_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.NORMALIZERS_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_OBJ_CLASS_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.OBJECT_CLASSES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_SYNTAX_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.SYNTAXES_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;
    private final static String RESOURCE_PATTERN_SUFFIX_SCHEMA_SYNTAX_CHECKER_FILES = DELIM_RESOURCE_PATTERN + SchemaConstants.SYNTAX_CHECKERS_PATH
        + RESOURCE_PATTERN_SUFFIX_LDIF_DATA_FILE;

    private final static String RESOURCE_PATTERN_SCHEMA_FILES = RESOURCE_PATTERN_PREFIX_SCHEMA + "*" + RESOURCE_PATTERN_SUFFIX_LDIF_FILE;

    private ResourcePatternResolver resourceLoader;

    @Override
    public List<Entry> loadAttributeTypes(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_ATTR_TYPE_FILES, schemas);
    }

    @Override
    public List<Entry> loadComparators(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_COMPARATOR_FILES, schemas);
    }

    @Override
    public List<Entry> loadDitContentRules(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_DIT_CONTENT_RULE_FILES, schemas);
    }

    @Override
    public List<Entry> loadDitStructureRules(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_DIT_STRUCT_RULE_FILES, schemas);
    }

    @Override
    public List<Entry> loadMatchingRules(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_MATCHING_RULE_FILES, schemas);
    }

    @Override
    public List<Entry> loadMatchingRuleUses(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_MATCHING_RULE_USE_FILES, schemas);
    }

    @Override
    public List<Entry> loadNameForms(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_NAME_FORM_FILES, schemas);
    }

    @Override
    public List<Entry> loadNormalizers(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_NORMALIZER_FILES, schemas);
    }

    @Override
    public List<Entry> loadObjectClasses(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_OBJ_CLASS_FILES, schemas);
    }

    @Override
    public List<Entry> loadSyntaxes(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_SYNTAX_FILES, schemas);
    }

    @Override
    public List<Entry> loadSyntaxCheckers(Schema ... schemas) throws LdapException, IOException {
        return this.loadSchemaResources(RESOURCE_PATTERN_SUFFIX_SCHEMA_SYNTAX_CHECKER_FILES, schemas);
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        Schema schema;

        for (Resource schemaResource : this.resourceLoader.getResources(RESOURCE_PATTERN_SCHEMA_FILES)) {
            this.schemaMap.put((schema = this.getSchema(this.readSchemaResource(schemaResource))).getSchemaName(), schema);
        }
    }

    private List<Entry> loadSchemaResources(String schemaResourcesSuffix, Schema[] schemas) throws LdapException, IOException {
        Map<Dn, Entry> schemaResourceEntryMap = new LinkedHashMap<>(schemas.length);
        Entry schemaResourceEntry;

        for (Schema schema : schemas) {
            for (Resource schemaResource : this.resourceLoader.getResources(RESOURCE_PATTERN_PREFIX_SCHEMA + schema.getSchemaName().toLowerCase()
                + schemaResourcesSuffix)) {
                schemaResourceEntryMap.put((schemaResourceEntry = this.readSchemaResource(schemaResource)).getDn(), schemaResourceEntry);
            }
        }

        return IteratorUtils.toList(schemaResourceEntryMap.values().iterator());
    }

    private Entry readSchemaResource(Resource schemaResource) throws LdapException, IOException {
        try (InputStream schemaResourceInStream = schemaResource.getInputStream()) {
            return ToolLdifUtils.readEntry(schemaResourceInStream).getEntry();
        }
    }

    @Override
    public void setResourceLoader(ResourceLoader resourceLoader) {
        this.resourceLoader = ((ResourcePatternResolver) resourceLoader);
    }
}
