package gov.hhs.onc.dcdt.ldap;

import gov.hhs.onc.dcdt.beans.ToolBean;
import javax.annotation.Nullable;
import org.apache.directory.api.ldap.model.name.Dn;

public interface LdapBindConfig extends ToolBean {
    public boolean isAnonymous();

    public void setAnonymous(boolean anon);

    public boolean hasBindDn();

    @Nullable
    public Dn getBindDn();

    public void setBindDn(@Nullable Dn bindDn);

    public boolean hasBindPassword();

    @Nullable
    public String getBindPassword();

    public void setBindPassword(@Nullable String bindPass);
}
