package gov.hhs.onc.dcdt.beans;

import org.springframework.context.Phased;

public interface OverrideablePhased extends Phased {
    public void setPhase(int phase);
}
