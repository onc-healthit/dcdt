package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupResult;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public abstract class AbstractLdapLookupResult<T> implements LdapLookupResult<T> {
    protected ResultCodeEnum code;
    protected LdapConnectionConfig connConfig;
    protected List<T> items;
    protected String msg;

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result) {
        this(connConfig, result, null);
    }

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result, @Nullable List<T> items) {
        this.connConfig = connConfig;
        this.code = result.getResultCode();
        this.msg = result.getDiagnosticMessage();
        this.items = items;
    }

    @Override
    public ResultCodeEnum getCode() {
        return this.code;
    }

    @Override
    public LdapConnectionConfig getConnectionConfig() {
        return this.connConfig;
    }

    @Override
    public boolean hasItems() {
        return !CollectionUtils.isEmpty(this.items);
    }

    @Nullable
    @Override
    public List<T> getItems() {
        return this.items;
    }

    @Override
    public boolean hasMessage() {
        return !StringUtils.isBlank(this.msg);
    }

    @Nullable
    @Override
    public String getMessage() {
        return this.msg;
    }

    @Override
    public boolean isSuccess() {
        return (this.code == ResultCodeEnum.SUCCESS);
    }
}
