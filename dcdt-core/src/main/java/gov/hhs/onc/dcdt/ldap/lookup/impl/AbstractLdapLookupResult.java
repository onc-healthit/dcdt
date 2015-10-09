package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.beans.ToolMessageLevel;
import gov.hhs.onc.dcdt.beans.ToolMessage;
import gov.hhs.onc.dcdt.beans.impl.AbstractToolLookupResultBean;
import gov.hhs.onc.dcdt.beans.impl.ToolMessageImpl;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupResult;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.io.Externalizable;
import java.util.Iterator;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;

public abstract class AbstractLdapLookupResult<T extends Externalizable> extends AbstractToolLookupResultBean<T, ResultCodeEnum> implements LdapLookupResult<T> {
    protected LdapConnectionConfig connConfig;
    protected LdapResult result;
    protected List<T> items;

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result) {
        this(connConfig, result, null);
    }

    protected AbstractLdapLookupResult(LdapConnectionConfig connConfig, LdapResult result, @Nullable List<T> items) {
        this.connConfig = connConfig;
        this.result = result;
        this.items = items;
    }

    @Override
    @SuppressWarnings({ "unchecked" })
    public Iterator<T> iterator() {
        return ((Iterator<T>) IteratorUtils.getIterator(this.items));
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
        ToolMessageLevel level = (this.isSuccess() ? ToolMessageLevel.INFO : ToolMessageLevel.ERROR);

        return ToolArrayUtils.asList(new ToolMessageImpl(level, this.getType().getMessage()),
            new ToolMessageImpl(level, this.result.getDiagnosticMessage()));
    }

    @Override
    public boolean isSuccess() {
        return (this.getType() == ResultCodeEnum.SUCCESS);
    }

    @Override
    public ResultCodeEnum getType() {
        return this.result.getResultCode();
    }
}
