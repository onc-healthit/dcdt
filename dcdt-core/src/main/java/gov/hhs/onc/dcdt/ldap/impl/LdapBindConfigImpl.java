package gov.hhs.onc.dcdt.ldap.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.ldap.LdapBindConfig;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.name.Dn;

public class LdapBindConfigImpl extends AbstractToolBean implements LdapBindConfig {
    private boolean anon;
    private Dn bindDn;
    private String bindPass;

    @Override
    public boolean isAnonymous() {
        return this.anon;
    }

    @Override
    public void setAnonymous(boolean anon) {
        this.anon = anon;
    }

    @Override
    public boolean hasBindDn() {
        return this.bindDn != null;
    }

    @Nullable
    @Override
    public Dn getBindDn() {
        return this.bindDn;
    }

    @Override
    public void setBindDn(@Nullable Dn bindDn) {
        this.bindDn = bindDn;
    }

    @Override
    public boolean hasBindPassword() {
        return !StringUtils.isBlank(this.bindPass);
    }

    @Nullable
    @Override
    public String getBindPassword() {
        return this.bindPass;
    }

    @Override
    public void setBindPassword(@Nullable String bindPass) {
        this.bindPass = bindPass;
    }
}
