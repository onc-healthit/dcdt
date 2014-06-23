package gov.hhs.onc.dcdt.dns;

import javax.annotation.Nonnegative;
import org.xbill.DNS.Section;

/**
 * @see org.xbill.DNS.Section
 */
public enum DnsMessageSection implements DnsMnemonicIdentifier {
    QUESTION(Section.QUESTION), ANSWER(Section.ANSWER), AUTHORITY(Section.AUTHORITY), ADDITIONAL(Section.ADDITIONAL);

    private final int code;
    private final String id;

    private DnsMessageSection(int code) {
        this.code = code;
        this.id = Section.string(code);
    }

    @Nonnegative
    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
