package gov.hhs.onc.dcdt.context.impl;

import org.springframework.context.support.DefaultLifecycleProcessor;
import org.springframework.stereotype.Component;

@Component("lifecycleProcessor")
public class ToolLifecycleProcessor extends DefaultLifecycleProcessor {
    @Override
    public void start() {
    }
}
