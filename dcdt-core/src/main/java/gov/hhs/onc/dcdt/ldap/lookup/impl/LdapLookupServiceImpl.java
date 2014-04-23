package gov.hhs.onc.dcdt.ldap.lookup.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.ldap.lookup.LdapBaseDnLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapEntryLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapAttributeUtils.LdapAttributeIdTransformer;
import gov.hhs.onc.dcdt.ldap.utils.ToolLdapFilterUtils;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import javax.annotation.Nullable;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.constants.SchemaConstants;
import org.apache.directory.api.ldap.model.cursor.EntryCursor;
import org.apache.directory.api.ldap.model.entry.Attribute;
import org.apache.directory.api.ldap.model.entry.Entry;
import org.apache.directory.api.ldap.model.exception.LdapAuthenticationException;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.filter.ExprNode;
import org.apache.directory.api.ldap.model.filter.ObjectClassNode;
import org.apache.directory.api.ldap.model.message.LdapResult;
import org.apache.directory.api.ldap.model.message.LdapResultImpl;
import org.apache.directory.api.ldap.model.message.ResultCodeEnum;
import org.apache.directory.api.ldap.model.message.SearchScope;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnection;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.apache.directory.ldap.client.api.LdapNetworkConnection;
import org.apache.directory.ldap.client.api.exception.InvalidConnectionException;
import org.springframework.stereotype.Component;

@Component("ldapLookupServiceImpl")
public class LdapLookupServiceImpl extends AbstractToolBean implements LdapLookupService {
    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable Attribute ... attrs) {
        return this.lookupEntries(connConfig, baseDn, ToolArrayUtils.asSet(attrs));
    }

    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable Set<Attribute> attrs) {
        return this.lookupEntries(connConfig, baseDn, null, attrs);
    }

    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable ExprNode filter, @Nullable Attribute ... attrs) {
        return this.lookupEntries(connConfig, baseDn, filter, ToolArrayUtils.asSet(attrs));
    }

    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable ExprNode filter, @Nullable Set<Attribute> attrs) {
        return this.lookupEntries(connConfig, baseDn, null, filter, attrs);
    }

    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable SearchScope scope, @Nullable ExprNode filter,
        @Nullable Attribute ... attrs) {
        return this.lookupEntries(connConfig, baseDn, scope, filter, ToolArrayUtils.asSet(attrs));
    }

    @Override
    public LdapEntryLookupResult lookupEntries(LdapConnectionConfig connConfig, Dn baseDn, @Nullable SearchScope scope, @Nullable ExprNode filter,
        @Nullable Set<Attribute> attrs) {
        scope = ObjectUtils.defaultIfNull(scope, SearchScope.SUBTREE);

        String filterExpr = ToolLdapFilterUtils.writeFilter((filter = ObjectUtils.defaultIfNull(filter, ObjectClassNode.OBJECT_CLASS_NODE)));
        LdapEntryLookupResult lookupResult;
        LdapConnection conn = null;

        try {
            conn = bind(connConfig, connect(connConfig));

            EntryCursor entryCursor =
                conn.search(
                    baseDn,
                    filterExpr,
                    scope,
                    (!CollectionUtils.isEmpty(attrs) ? ToolCollectionUtils.toArray(CollectionUtils.collect(attrs, LdapAttributeIdTransformer.INSTANCE),
                        String.class) : ArrayUtils.EMPTY_STRING_ARRAY));
            List<Entry> entries = IteratorUtils.toList(entryCursor.iterator());

            entryCursor.close();

            lookupResult = new LdapEntryLookupResultImpl(connConfig, baseDn, scope, filter, attrs, entryCursor.getSearchResultDone().getLdapResult(), entries);
        } catch (LdapException e) {
            lookupResult = new LdapEntryLookupResultImpl(connConfig, baseDn, scope, filter, attrs, buildResult(e));
        } finally {
            if (conn != null) {
                try {
                    disconnect(connConfig, unBind(connConfig, conn));
                } catch (LdapException e) {
                    lookupResult = new LdapEntryLookupResultImpl(connConfig, baseDn, scope, filter, attrs, buildResult(e));
                }
            }
        }

        return lookupResult;
    }

    @Override
    public LdapBaseDnLookupResult lookupBaseDns(LdapConnectionConfig connConfig) {
        LdapBaseDnLookupResult lookupResult;
        LdapConnection conn = null;

        try {
            conn = bind(connConfig, connect(connConfig));

            Entry baseDnEntry = conn.getRootDse(SchemaConstants.NAMING_CONTEXTS_AT);
            List<Dn> baseDns = new ArrayList<>(baseDnEntry.size());

            for (Attribute baseDnAttr : baseDnEntry) {
                baseDns.add(new Dn(baseDnAttr.getString()));
            }

            lookupResult = new LdapBaseDnLookupResultImpl(connConfig, new LdapResultImpl(), baseDns);
        } catch (LdapException e) {
            lookupResult = new LdapBaseDnLookupResultImpl(connConfig, buildResult(e));
        } finally {
            if (conn != null) {
                try {
                    disconnect(connConfig, unBind(connConfig, conn));
                } catch (LdapException e) {
                    lookupResult = new LdapBaseDnLookupResultImpl(connConfig, buildResult(e));
                }
            }
        }

        return lookupResult;
    }

    private static LdapConnection disconnect(LdapConnectionConfig connConfig, LdapConnection conn) throws LdapException {
        if (conn.isConnected()) {
            try {
                conn.close();
            } catch (IOException e) {
                throw new InvalidConnectionException(String.format("Unable to disconnect from LDAP server (host=%s, port=%d, ssl=%s).",
                    connConfig.getLdapHost(), connConfig.getLdapPort(), connConfig.isUseSsl()), e);
            }
        }

        return conn;
    }

    private static LdapConnection unBind(LdapConnectionConfig connConfig, LdapConnection conn) throws LdapException {
        if (conn.isAuthenticated()) {
            conn.unBind();

            if (conn.isAuthenticated()) {
                throw new LdapAuthenticationException(String.format("Unable to unbind (dn={%s}) from LDAP server (host=%s, port=%d, ssl=%s).",
                    connConfig.getName(), connConfig.getLdapHost(), connConfig.getLdapPort(), connConfig.getSslProtocol()));
            }
        }

        return conn;
    }

    private static LdapConnection bind(LdapConnectionConfig connConfig, LdapConnection conn) throws LdapException {
        if (StringUtils.isBlank(connConfig.getName()) || StringUtils.isBlank(connConfig.getCredentials())) {
            conn.anonymousBind();
        } else {
            conn.bind();
        }

        if (!conn.isAuthenticated()) {
            throw new LdapAuthenticationException(String.format("Unable to bind (dn={%s}) to LDAP server (host=%s, port=%d, ssl=%s).", connConfig.getName(),
                connConfig.getLdapHost(), connConfig.getLdapPort(), connConfig.getSslProtocol()));
        }

        return conn;
    }

    private static LdapConnection connect(LdapConnectionConfig connConfig) throws LdapException {
        LdapConnection conn = new LdapNetworkConnection(connConfig);

        if (!conn.connect()) {
            throw new InvalidConnectionException(String.format("Unable to connect to LDAP server (host=%s, port=%d, ssl=%s).", connConfig.getLdapHost(),
                connConfig.getLdapPort(), connConfig.isUseSsl()));
        }

        return conn;
    }

    private static LdapResult buildResult(LdapException exception) {
        LdapResult result = new LdapResultImpl();
        result.setDiagnosticMessage(exception.getMessage());
        result.setResultCode(ResultCodeEnum.getBestEstimate(exception, null));

        return result;
    }
}
