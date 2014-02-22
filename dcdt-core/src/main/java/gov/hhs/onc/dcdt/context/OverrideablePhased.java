package gov.hhs.onc.dcdt.context;

import org.springframework.context.Phased;

public interface OverrideablePhased extends Phased {
    public void setPhase(int phase);
}
