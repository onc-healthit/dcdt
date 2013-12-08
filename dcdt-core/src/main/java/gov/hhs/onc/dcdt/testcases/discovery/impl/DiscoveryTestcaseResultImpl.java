package gov.hhs.onc.dcdt.testcases.discovery.impl;

import gov.hhs.onc.dcdt.testcases.discovery.DiscoveryTestcaseResult;
import gov.hhs.onc.dcdt.testcases.impl.AbstractToolTestcaseResult;
import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("discoveryTestcaseResultImpl")
@Lazy
@Scope("prototype")
public class DiscoveryTestcaseResultImpl extends AbstractToolTestcaseResult implements DiscoveryTestcaseResult {
}
