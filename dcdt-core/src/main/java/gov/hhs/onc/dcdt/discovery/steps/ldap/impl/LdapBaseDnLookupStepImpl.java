package gov.hhs.onc.dcdt.discovery.steps.ldap.impl;

import gov.hhs.onc.dcdt.discovery.BindingType;
import gov.hhs.onc.dcdt.discovery.steps.CertificateDiscoveryStep;
import gov.hhs.onc.dcdt.discovery.steps.dns.DnsSrvRecordLookupStep;
import gov.hhs.onc.dcdt.dns.DnsException;
import gov.hhs.onc.dcdt.dns.lookup.DnsLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapBaseDnLookupResult;
import gov.hhs.onc.dcdt.ldap.lookup.LdapLookupService;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.utils.ToolCollectionUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.util.List;
import javax.annotation.Nullable;
import org.apache.commons.lang3.ArrayUtils;
import org.apache.directory.api.ldap.model.name.Dn;
import org.apache.directory.ldap.client.api.LdapConnectionConfig;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.xbill.DNS.ARecord;
import org.xbill.DNS.Name;
import org.xbill.DNS.SRVRecord;

public class LdapBaseDnLookupStepImpl extends AbstractLdapLookupStep<Dn, LdapBaseDnLookupResult> {
    private final static Logger LOGGER = LoggerFactory.getLogger(LdapBaseDnLookupStepImpl.class);

    public LdapBaseDnLookupStepImpl(LdapLookupService lookupService) {
        super(BindingType.DOMAIN, lookupService);
    }

    @Override
    public boolean execute(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        return super.execute(prevSteps, directAddr);
    }

    @Nullable
    @Override
    protected LdapBaseDnLookupResult executeLookup(List<CertificateDiscoveryStep> prevSteps, MailAddress directAddr) {
        DnsSrvRecordLookupStep srvRecordLookupStep = ToolCollectionUtils.findAssignable(DnsSrvRecordLookupStep.class, prevSteps);
        LdapBaseDnLookupResult lookupResult;

        // noinspection ConstantConditions
        if (srvRecordLookupStep.isSuccess()) {
            DnsLookupResult<SRVRecord> srvRecordLookupResult;
            DnsLookupResult<ARecord> srvRecordTargetLookupResult;
            Name connName = null, connTarget = null;
            int connPort = -1;
            String connHost;
            LdapConnectionConfig connConfig;

            // noinspection ConstantConditions
            if ((srvRecordLookupResult = srvRecordLookupStep.getResult()).hasOrderedAnswers()) {
                // noinspection ConstantConditions
                for (SRVRecord srvRecord : srvRecordLookupResult.getOrderedAnswers()) {
                    try {
                        connName = srvRecord.getName();
                        connPort = srvRecord.getPort();

                        if ((srvRecordTargetLookupResult = srvRecordLookupStep.getLookupService().lookupARecords((connTarget = srvRecord.getTarget())))
                            .isSuccess() && srvRecordTargetLookupResult.hasAnswers()) {
                            // noinspection ConstantConditions
                            LOGGER.trace(String.format("DNS SRV record (name=%s, target=%s, port=%d) target address resolution was successful: [%s]", connName,
                                connTarget, connPort, (connHost =
                                    ToolListUtils.getFirst(srvRecordTargetLookupResult.getAnswers()).getAddress().getHostAddress())));
                        } else {
                            this.execMsgs.add(String.format("DNS SRV record (name=%s, target=%s, port=%d) target address resolution failed: [%s]", connName,
                                connTarget, connPort, ToolStringUtils.joinDelimit(srvRecordTargetLookupResult.getMessages(), ", ")));

                            continue;
                        }

                        connConfig = new LdapConnectionConfig();
                        connConfig.setLdapPort(connPort);
                        connConfig.setLdapHost(connHost);

                        if ((lookupResult = this.lookupService.lookupBaseDns(connConfig)).isSuccess()) {
                            LOGGER.trace(String.format("LDAP base Distinguished Name (DN) lookup (host=%s, port=%d) was successful: [%s]", connHost, connPort,
                                ToolStringUtils.joinDelimit(lookupResult, ", ")));

                            return lookupResult;
                        } else {
                            this.execMsgs.add(String.format("Attempted LDAP base Distinguished Name (DN) lookup (host=%s, port=%d) failed: [%s]", connHost,
                                connPort, ToolStringUtils.joinDelimit(lookupResult.getMessages(), ", ")));
                        }
                    } catch (DnsException e) {
                        this.execMsgs.add(String.format("DNS SRV record (name=%s, target=%s, port=%d) processing failed: %s", connName, connTarget, connPort,
                            e.getMessage()));
                        this.execSuccess = false;

                        break;
                    }
                }
            }
        }

        return null;
    }

    @Override
    protected Object[] getCloneArguments() {
        return ArrayUtils.toArray(this.lookupService);
    }
}
