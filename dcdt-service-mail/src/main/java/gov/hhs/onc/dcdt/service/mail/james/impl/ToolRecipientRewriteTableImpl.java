package gov.hhs.onc.dcdt.service.mail.james.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.service.mail.james.ToolRecipientRewriteTable;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.Nullable;
import org.apache.james.rrt.api.RecipientRewriteTableException;

public class ToolRecipientRewriteTableImpl extends AbstractToolBean implements ToolRecipientRewriteTable {
    @Override
    public void addRegexMapping(String userName, String domainNameStr, String regexMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to add regular expression mapping (userName=%s, domainName=%s) to James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), regexMapping));
    }

    @Override
    public void removeRegexMapping(String userName, String domainNameStr, String regexMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to remove regular expression mapping (userName=%s, domainName=%s) from James recipient rewrite table (class=%s): %s", userName,
            domainNameStr, ToolClassUtils.getName(this), regexMapping));
    }

    @Override
    public void addAddressMapping(String userName, String domainNameStr, String addrMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to add address mapping (userName=%s, domainName=%s) to James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), addrMapping));
    }

    @Override
    public void removeAddressMapping(String userName, String domainNameStr, String addrMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to remove address mapping (userName=%s, domainName=%s) from James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), addrMapping));
    }

    @Override
    public void addErrorMapping(String userName, String domainNameStr, String errorMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to add error mapping (userName=%s, domainName=%s) to James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), errorMapping));
    }

    @Override
    public void removeErrorMapping(String userName, String domainNameStr, String errorMapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to remove error mapping (userName=%s, domainName=%s) from James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), errorMapping));
    }

    @Override
    public void addMapping(String userName, String domainNameStr, String mapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to add mapping (userName=%s, domainName=%s) to James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), mapping));
    }

    @Override
    public void removeMapping(String userName, String domainNameStr, String mapping) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to remove mapping (userName=%s, domainName=%s) from James recipient rewrite table (class=%s): %s", userName, domainNameStr,
            ToolClassUtils.getName(this), mapping));
    }

    @Override
    public void addAliasDomainMapping(String aliasDomainNameStr, String domainNameStr) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to add domain mapping (aliasName=%s, name=%s) to James recipient rewrite table (class=%s).", aliasDomainNameStr, domainNameStr,
            ToolClassUtils.getName(this)));
    }

    @Override
    public void removeAliasDomainMapping(String aliasDomainNameStr, String domainNameStr) throws RecipientRewriteTableException {
        throw new RecipientRewriteTableException(String.format(
            "Unable to remove domain mapping (aliasName=%s, name=%s) from James recipient rewrite table (class=%s).", aliasDomainNameStr, domainNameStr,
            ToolClassUtils.getName(this)));
    }

    @Override
    public Map<String, Collection<String>> getAllMappings() throws RecipientRewriteTableException {
        return new HashMap<>(0);
    }

    @Nullable
    @Override
    public Collection<String> getMappings(String userName, String domainNameStr) throws ErrorMappingException, RecipientRewriteTableException {
        return null;
    }

    @Nullable
    @Override
    public Collection<String> getUserDomainMappings(String userName, String domainNameStr) throws RecipientRewriteTableException {
        return null;
    }
}
