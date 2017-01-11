package gov.hhs.onc.dcdt.mail.smtp.command.impl;

import gov.hhs.onc.dcdt.dns.utils.ToolDnsNameUtils;
import gov.hhs.onc.dcdt.mail.MailAddress;
import gov.hhs.onc.dcdt.mail.impl.MailAddressImpl;
import gov.hhs.onc.dcdt.mail.smtp.SmtpCommandException;
import gov.hhs.onc.dcdt.mail.smtp.SmtpReplyCode;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommand;
import gov.hhs.onc.dcdt.mail.smtp.command.SmtpCommandType;
import gov.hhs.onc.dcdt.mail.smtp.impl.AbstractSmtpMessage;
import gov.hhs.onc.dcdt.mail.smtp.impl.SmtpReplyImpl;
import gov.hhs.onc.dcdt.mail.utils.ToolMailAddressUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils.ToolStrBuilder;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.apache.commons.lang3.StringUtils;

public abstract class AbstractSmtpCommand extends AbstractSmtpMessage<SmtpCommandType> implements SmtpCommand {
    public final static int MAX_PATH_LEN = 256;

    public final static String PATH_DELIM = ":";
    public final static String PATH_ROUTE_DELIM = ",";

    public final static String PATTERN_STR_PATH_ROUTE = "(?:" + ToolMailAddressUtils.MAIL_ADDR_PART_DELIM + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_REL_BASE
        + PATH_ROUTE_DELIM + "?)(?<=[" + PATH_ROUTE_DELIM + ToolMailAddressUtils.MAIL_ADDR_PART_DELIM + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY
        + "]{1,253})(?<!" + PATH_ROUTE_DELIM + ")";

    public final static String PATTERN_STR_PATH = "^(?:" + "(?:" + PATTERN_STR_PATH_ROUTE + ")+" + PATH_DELIM + ")?("
        + ToolMailAddressUtils.PATTERN_STR_MAIL_ADDR_BASE + ")(?<=[" + PATH_DELIM + PATH_ROUTE_DELIM + ToolMailAddressUtils.MAIL_ADDR_PART_DELIM
        + ToolDnsNameUtils.PATTERN_STR_DNS_NAME_LBL_CHAR_ANY + "]{1," + MAX_PATH_LEN + "})$";

    public final static Pattern PATTERN_PATH = Pattern.compile(PATTERN_STR_PATH);

    protected AbstractSmtpCommand(SmtpCommandType type) {
        super(type);
    }

    @Override
    public String toString() {
        ToolStrBuilder builder = this.parametersToString(new ToolStrBuilder());

        if (!builder.isEmpty()) {
            builder.insert(0, StringUtils.SPACE);
        }

        builder.insert(0, this.type.getId());
        builder.append(ToolStringUtils.CRLF);

        return builder.build();
    }

    protected static MailAddress parsePath(int maxNumParams, String prefix, String str) throws SmtpCommandException {
        String[] params = parseParameters(1, maxNumParams, str);
        
        for (int j=0; j<params.length;j++){
        	System.out.println("Params"+ j+": "+params[0]);
        }
        
        if (!(StringUtils.startsWith(params[0].toUpperCase(), prefix) || !StringUtils.endsWith(params[0].toUpperCase(), ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX))) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Required syntax: '%slocal@domain%s'", prefix,
                ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX)));
        }

        int pathLen =
            (params[0] = StringUtils.removeEnd(StringUtils.removeStart(params[0], prefix), ToolMailAddressUtils.MAIL_ADDR_PART_PERSONAL_ADDR_SUFFIX)).length();

        if (pathLen > MAX_PATH_LEN) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Path too long: %d > %d", pathLen,
                MAX_PATH_LEN)));
        }
        String paramsU = params[0].toUpperCase();
        if (paramsU.contains("FROM")){
        paramsU = paramsU.replace("FROM:<", "");
        }
        if (paramsU.contains("TO")){
            paramsU = paramsU.replace("TO:<", "");
            }
        Matcher pathMatcher = PATTERN_PATH.matcher(paramsU);
        System.out.println("111111  :" + paramsU);
        if (!pathMatcher.matches()) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Malformed email address: %s", params[0].toUpperCase())));
        }
        System.out.println("2222222   "+new MailAddressImpl(pathMatcher.group(1)));
        return new MailAddressImpl(pathMatcher.group(1));
    }

    protected static String[] parseParameters(int numParams, String str) throws SmtpCommandException {
        return parseParameters(numParams, numParams, str);
    }

    protected static String[] parseParameters(int minNumParams, int maxNumParams, String str) throws SmtpCommandException {
        return parseParameters((strSplit) -> StringUtils.split(strSplit, StringUtils.SPACE), minNumParams, maxNumParams, str);
    }

    protected static String[] parseParameters(Function<String, String[]> splitter, int numParams, String str) throws SmtpCommandException {
        return parseParameters(splitter, numParams, numParams, str);
    }

    protected static String[] parseParameters(Function<String, String[]> splitter, int minNumParams, int maxNumParams, String str) throws SmtpCommandException {
        String[] params = splitter.apply(str);

        if ((minNumParams >= 0) && (params.length < minNumParams)) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Too few parameters: %d < %d", params.length,
                minNumParams)));
        }

        if ((maxNumParams >= 0) && (params.length > maxNumParams)) {
            throw new SmtpCommandException(new SmtpReplyImpl(SmtpReplyCode.SYNTAX_ERROR_ARGUMENTS, String.format("Too many parameters: %d > %d", params.length,
                maxNumParams)));
        }

        return params;
    }

    protected ToolStrBuilder parametersToString(ToolStrBuilder builder) {
        return builder;
    }
}
