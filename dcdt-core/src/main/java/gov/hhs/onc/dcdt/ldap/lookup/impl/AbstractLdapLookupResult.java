package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLookupResultBean;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupResult;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.io.Externalizable;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public abstract class AbstractLdapLookupResult<T extends Externalizable> extends AbstractToolLookupResultBean implements LdapLookupResult<T> {
    protected LdapConnectionConfig connConfig;
    protected LdapResult result;
    protected List<T> items;
    protected List<ToolMessage> msgs;

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result) {
        this(connConfig, result, null);
    }

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result, @Nullable List<T> items) {
        this.connConfig = connConfig;
        this.result = result;
        this.items = items;

        ResultCodeEnum code = this.getCode();

        this.msgs = ToolArrayUtils.asList(new ToolMessageImpl((this.isSuccess() ? ToolMessageLevel.INFO : ToolMessageLevel.ERROR),
            String.format("code=%d, msg=%s, diagnosticMsg=%s", code.getResultCode(), code.getMessage(), this.result.getDiagnosticMessage())));
    }

    @Override
    public ResultCodeEnum getCode() {
        return this.result.getResultCode();
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

    @Nullable
    @Override
    public List<ToolMessage> getMessages() {
        return this.msgs;
    }

    @Override
    public boolean isSuccess() {
        return (this.getCode() == ResultCodeEnum.SUCCESS);
    }
}
