package gov.hhs.onc.dcdt.service.ldap.config.impl;

import org.apache.directory.api.ldap.model.schema.SchemaManager;
import org.apache.directory.server.config.beans.DirectoryServiceBean;

public class ToolDirectoryServiceBean extends DirectoryServiceBean {
    private ToolInstanceLayout instanceLayout;
    private SchemaManager schemaManager;

    public ToolInstanceLayout getInstanceLayout() {
        return this.instanceLayout;
    }

    public void setInstanceLayout(ToolInstanceLayout instanceLayout) {
        this.instanceLayout = instanceLayout;
    }

    public SchemaManager getSchemaManager() {
        return this.schemaManager;
    }

    public void setSchemaManager(SchemaManager schemaManager) {
        this.schemaManager = schemaManager;
    }
}
