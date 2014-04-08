package gov.hhs.onc.dcdt.ldap.lookup;

import java.util.List;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public interface LdapLookupResult<T> {
    public ResultCodeEnum getCode();

    public LdapConnectionConfig getConnectionConfig();

    public boolean hasItems();

    @Nullable
    public List<T> getItems();

    public boolean hasMessage();

    @Nullable
    public String getMessage();

    public boolean isSuccess();
}
