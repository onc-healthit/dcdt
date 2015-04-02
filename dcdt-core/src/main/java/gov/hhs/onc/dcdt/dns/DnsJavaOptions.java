package gov.hhs.onc.dcdt.dns;

import java.util.Map;
import javax.annotation.Nullable;
import org.apache.commons.collections4.MapUtils;
import org.xbill.DNS.Options;

public final class DnsJavaOptions {
    public final static String BIND_TTL = "bindttl";

    public final static String MULTILINE = "multiline";

    private DnsJavaOptions() {
    }

    public static void fromMap(@Nullable Map<String, String> optMap) {
        Options.clear();

        if (!MapUtils.isEmpty(optMap)) {
            optMap.keySet().forEach(optName -> Options.set(optName, optMap.get(optName)));
        }
    }
}
