package gov.hhs.onc.dcdt.ldap.lookup;

import gov.hhs.onc.dcdt.beans.ToolLookupResultBean;
import java.io.Externalizable;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface LdapLookupResult<T extends Externalizable> extends ToolLookupResultBean {
    public ResultCodeEnum getCode();

    public LdapConnectionConfig getConnectionConfig();

    public boolean hasItems();

    @Nullable
    public List<T> getItems();
}
