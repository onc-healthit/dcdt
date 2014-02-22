package gov.hhs.onc.dcdt.dns;

import org.xbill.DNS.Section;

/**
 * @see org.xbill.DNS.Section
 */
public enum DnsMessageSection {
    QUESTION(Section.QUESTION), ANSWER(Section.ANSWER), AUTHORITY(Section.AUTHORITY), ADDITIONAL(Section.ADDITIONAL), ZONE(Section.ZONE),
    PREREQ(Section.PREREQ), UPDATE(Section.UPDATE);

    private final int section;

    private DnsMessageSection(int section) {
        this.section = section;
    }

    public int getSection() {
        return this.section;
    }
}
