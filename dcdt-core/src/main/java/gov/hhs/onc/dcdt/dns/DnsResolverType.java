package gov.hhs.onc.dcdt.dns;


import org.springframework.context.annotation.Lazy;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

@Component("toolDnsResolverType")
@Lazy
@Scope("singleton")
public enum DnsResolverType {
    LOCAL("local"), EXTERNAL("external");

    private String type;

    DnsResolverType(String type) {
        this.type = type;
    }

    @Override
    public String toString() {
        return this.type;
    }

    public String getType() {
        return this.type;
    }
}
