package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.ldap.lookup.ToolLdapLookupException;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils.LdapAttributeIdTransformer;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.springframework.stereotype.Component;

@Component("ldapLookupServiceImpl")
public class LdapLookupServiceImpl extends AbstractToolBean implements LdapLookupService {
    private final static class CloseableLdapConnectionWrapper implements AutoCloseable {
        private LdapConnectionConfig ldapConnConfig;
        private LdapConnection ldapConn;

        private CloseableLdapConnectionWrapper(LdapConnectionConfig ldapConnConfig) {
            this.ldapConnConfig = ldapConnConfig;
        }

        @Override
        public void close() throws Exception {
            if (this.ldapConn != null) {
                disconnect(this.ldapConnConfig, unBind(this.ldapConnConfig, this.ldapConn));
            }
        }

        public void setLdapConnection(LdapConnection ldapConn) {
            this.ldapConn = ldapConn;
        }
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, Attribute ... searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, ToolArrayUtils.asList(searchAttrs));
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, Iterable<Attribute> searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, null, searchAttrs);
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable ExprNode searchFilter, Attribute ... searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, searchFilter, ToolArrayUtils.asList(searchAttrs));
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable ExprNode searchFilter, Iterable<Attribute> searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, null, searchFilter, searchAttrs);
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Attribute ... searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, searchScope, searchFilter, ToolArrayUtils.asList(searchAttrs));
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Iterable<Attribute> searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, null, searchScope, searchFilter, searchAttrs);
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable Dn baseDn, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Attribute ... searchAttrs) throws ToolLdapException {
        return this.search(ldapConnConfig, baseDn, searchScope, searchFilter, ToolArrayUtils.asList(searchAttrs));
    }

    @Override
    public List<Entry> search(LdapConnectionConfig ldapConnConfig, @Nullable Dn baseDn, @Nullable SearchScope searchScope, @Nullable ExprNode searchFilter,
        Iterable<Attribute> searchAttrs) throws ToolLdapException {
        searchScope = (searchScope != null) ? searchScope : SearchScope.SUBTREE;

        if (baseDn != null) {
            return this.searchInternal(ldapConnConfig, baseDn, searchScope, searchFilter, searchAttrs);
        } else {
            List<Entry> searchResults = new ArrayList<>();

            for (Dn baseDnFound : this.getBaseDns(ldapConnConfig)) {
                searchResults.addAll(this.searchInternal(ldapConnConfig, baseDnFound, searchScope, searchFilter, searchAttrs));
            }

            return searchResults;
        }
    }

    @Override
    public List<Dn> getBaseDns(LdapConnectionConfig ldapConnConfig) throws ToolLdapException {
        LdapConnection ldapConn;

        try (CloseableLdapConnectionWrapper ldapConnWrapper = new CloseableLdapConnectionWrapper(ldapConnConfig)) {
            ldapConnWrapper.setLdapConnection(ldapConn = bind(ldapConnConfig, connect(ldapConnConfig)));

            Collection<Attribute> baseDnsAttrs = ldapConn.getRootDse(SchemaConstants.NAMING_CONTEXTS_AT).getAttributes();
            List<Dn> baseDns = new ArrayList<>(baseDnsAttrs.size());

            for (Attribute baseDnAttr : baseDnsAttrs) {
                baseDns.add(new Dn(baseDnAttr.getString()));
            }

            return baseDns;
        } catch (Exception e) {
            throw new ToolLdapLookupException(String.format("Unable to get base DN(s) in LDAP server (host=%s, port=%d, ssl=%s).",
                ldapConnConfig.getLdapHost(), ldapConnConfig.getLdapPort(), ldapConnConfig.isUseSsl()), e);
        }
    }

    private static LdapConnection disconnect(LdapConnectionConfig ldapConnConfig, LdapConnection ldapConn) throws ToolLdapException {
        if (ldapConn.isConnected()) {
            try {
                ldapConn.close();
            } catch (IOException e) {
                throw new ToolLdapLookupException(String.format("Unable to disconnect from LDAP server (host=%s, port=%d, ssl=%s).",
                    ldapConnConfig.getLdapHost(), ldapConnConfig.getLdapPort(), ldapConnConfig.isUseSsl()), e);
            }
        }

        return ldapConn;
    }

    private static LdapConnection unBind(LdapConnectionConfig ldapConnConfig, LdapConnection ldapConn) throws ToolLdapException {
        if (ldapConn.isAuthenticated()) {
            try {
                ldapConn.unBind();
            } catch (LdapException e) {
                throw new ToolLdapLookupException(String.format("Unable to unbind from LDAP server (host=%s, port=%d, ssl=%s).", ldapConnConfig.getLdapHost(),
                    ldapConnConfig.getLdapPort(), ldapConnConfig.getSslProtocol()), e);
            }
        }

        return ldapConn;
    }

    private static LdapConnection bind(LdapConnectionConfig ldapConnConfig, LdapConnection ldapConn) throws ToolLdapException {
        try {
            if (StringUtils.isBlank(ldapConnConfig.getName())) {
                ldapConn.anonymousBind();
            } else {
                ldapConn.bind();
            }
        } catch (LdapException e) {
            throw new ToolLdapLookupException(String.format("Unable to bind (bindDn=%s) to LDAP server (host=%s, port=%d, ssl=%s).", ldapConnConfig.getName(),
                ldapConnConfig.getLdapHost(), ldapConnConfig.getLdapPort(), ldapConnConfig.getSslProtocol()), e);
        }

        return ldapConn;
    }

    private static LdapConnection connect(LdapConnectionConfig ldapConnConfig) throws ToolLdapException {
        try {
            LdapConnection ldapConn = new LdapNetworkConnection(ldapConnConfig);
            ldapConn.connect();

            return ldapConn;
        } catch (LdapException e) {
            throw new ToolLdapLookupException(String.format("Unable to connect to LDAP server (host=%s, port=%d, ssl=%s).", ldapConnConfig.getLdapHost(),
                ldapConnConfig.getLdapPort(), ldapConnConfig.getSslProtocol()), e);
        }
    }

    private List<Entry> searchInternal(LdapConnectionConfig ldapConnConfig, Dn baseDn, SearchScope searchScope, @Nullable ExprNode searchFilter,
        Iterable<Attribute> searchAttrs) throws ToolLdapException {
        String searchFilterExpr = Objects.toString(searchFilter, null);
        String[] searchAttrIds = ToolCollectionUtils.toArray(CollectionUtils.collect(searchAttrs, LdapAttributeIdTransformer.INSTANCE), String.class);
        LdapConnection ldapConn;

        try (CloseableLdapConnectionWrapper ldapConnWrapper = new CloseableLdapConnectionWrapper(ldapConnConfig)) {
            ldapConnWrapper.setLdapConnection(ldapConn = bind(ldapConnConfig, connect(ldapConnConfig)));

            EntryCursor searchResultsCursor = ldapConn.search(baseDn, searchFilterExpr, searchScope, searchAttrIds);
            List<Entry> searchResults = new ArrayList<>();
            Entry searchResult;

            while (searchResultsCursor.next() && ((searchResult = searchResultsCursor.get()) != null)) {
                searchResults.add(searchResult);
            }

            searchResultsCursor.close();

            ResultCodeEnum searchResultCode;

            if ((searchResultCode = searchResultsCursor.getSearchResultDone().getLdapResult().getResultCode()) != ResultCodeEnum.SUCCESS) {
                throw new ToolLdapLookupException(String.format(
                    "Unable to search (baseDn=%s, scope=%s, filter=%s, attrs=[%s]) LDAP server (host=%s, port=%d, ssl=%s): %s", baseDn, searchScope,
                    searchFilterExpr, ToolStringUtils.joinDelimit(searchAttrIds, ","), ldapConnConfig.getLdapHost(), ldapConnConfig.getLdapPort(),
                    ldapConnConfig.isUseSsl(), searchResultCode.name()));
            }

            return searchResults;
        } catch (Exception e) {
            throw new ToolLdapLookupException(String.format(
                "Unable to search (baseDn=%s, scope=%s, filter=%s, attrIds=[%s]) LDAP server (host=%s, port=%d, ssl=%s).", baseDn, searchScope,
                searchFilterExpr, ToolStringUtils.joinDelimit(searchAttrIds, ","), ldapConnConfig.getLdapHost(), ldapConnConfig.getLdapPort(),
                ldapConnConfig.isUseSsl()), e);
        }
    }
}
