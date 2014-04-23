package gov.hhs.onc.dcdt.service.dns.server;

import gov.hhs.onc.dcdt.nio.channels.ChannelListenerSelector;
import org.springframework.context.ApplicationContextAware;

public interface DnsServerChannelListenerSelector extends ApplicationContextAware, ChannelListenerSelector {
}
