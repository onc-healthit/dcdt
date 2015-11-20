package gov.hhs.onc.dcdt.mail.smtp;

import gov.hhs.onc.dcdt.beans.ToolCodeIdentifier;
import gov.hhs.onc.dcdt.beans.ToolIdentifier;

public enum SmtpReplyCode implements ToolCodeIdentifier, ToolIdentifier {
    SYSTEM_STATUS(211), HELP_MESSAGE(214), SERVICE_READY(220), SYSTEM_QUIT(221), AUTH_OK(235), MAIL_OK(250), MAIL_FORWARD(251), MAIL_UNDEFINED(252),
    AUTH_READY(334), DATA_READY(354), SERVICE_UNAVAILABLE(421), MAILBOX_TEMP_UNAVAILABLE(450), LOCAL_ERROR(451), SYSTEM_STORAGE_ERROR(452),
    AUTH_TEMP_ERROR(454), PARAMETER_ERROR(455), SYNTAX_ERROR_COMMAND(500), SYNTAX_ERROR_ARGUMENTS(501), COMMAND_UNIMPLEMENTED(502), BAD_SEQUENCE(503),
    PARAMETER_UNIMPLEMENTED(504), AUTH_REQUIRED(530), AUTH_FAILED(535), AUTH_MECHANISM_WEAK(534), AUTH_ENCRYPTION_REQUIRED(538), MAILBOX_PERM_UNAVAILABLE(550),
    USER_NOT_LOCAL(551), SYSTEM_STORAGE_EXCEEDED(552), SYNTAX_ERROR_MAILBOX(553), TRANSACTION_FAILED(554), MAIL_PARAMETERS_UNIMPLEMENTED(555);

    private final int code;
    private final String id;

    private SmtpReplyCode(int code) {
        this.id = Integer.toString((this.code = code));
    }

    @Override
    public int getCode() {
        return this.code;
    }

    @Override
    public String getId() {
        return this.id;
    }
}
