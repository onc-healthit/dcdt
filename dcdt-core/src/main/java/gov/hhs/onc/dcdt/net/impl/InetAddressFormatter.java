package gov.hhs.onc.dcdt.net.impl;

import gov.hhs.onc.dcdt.format.impl.AbstractToolFormatter;
import gov.hhs.onc.dcdt.net.utils.ToolInetAddressUtils;
import java.net.InetAddress;
import java.util.Locale;
import javax.annotation.Nullable;
import org.springframework.stereotype.Component;

@Component("formatterInetAddr")
public class InetAddressFormatter extends AbstractToolFormatter<InetAddress> {
    public InetAddressFormatter() {
        super(InetAddress.class);
    }

    @Nullable
    @Override
    protected String printInternal(InetAddress obj, Locale locale) throws Exception {
        return obj.getHostAddress();
    }

    @Nullable
    @Override
    protected InetAddress parseInternal(String str, Locale locale) throws Exception {
        return ToolInetAddressUtils.getByAddress(str);
    }
}
