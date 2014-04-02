package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.nio.channels.SelectionAttachment;
import javax.annotation.Nonnegative;

public interface DnsServerQueryAttachment extends SelectionAttachment {
    public boolean hasQuerySize();

    public int getQuerySize();

    @Nonnegative
    public int setQuerySize(@Nonnegative int querySize);
}
