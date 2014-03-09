package gov.hhs.onc.dcdt.service.ldap.schemaloader;

import org.apache.directory.api.ldap.model.schema.registries.SchemaLoader;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.context.ResourceLoaderAware;

public interface ToolJarLdifSchemaLoader extends InitializingBean, ResourceLoaderAware, SchemaLoader {
}
