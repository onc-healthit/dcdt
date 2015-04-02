package gov.hhs.onc.dcdt.mail.impl;

import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.FilterOutputStream;
import java.io.OutputStream;
import javax.mail.MessagingException;

/**
 * @see org.bouncycastle.mail.smime.SMIMEUtil
 */
public class LineOutputStream extends FilterOutputStream {
    private static byte[] newLine;

    static {
        newLine = new byte[2];
        newLine[0] = (byte) ('\r');
        newLine[1] = (byte) ('\n');
    }

    public LineOutputStream(OutputStream outputStream) {
        super(outputStream);
    }

    public void writeln(String str) throws MessagingException {
        try {
            byte[] byteArr = getBytes(str);
            super.out.write(byteArr);
            this.writeln();
        } catch (Exception e) {
            throw new MessagingException(String.format("Unable to write line to output stream (%s):", ToolClassUtils.getName(out)), e);
        }
    }

    public void writeln() throws MessagingException {
        try {
            super.out.write(newLine);
        } catch (Exception e) {
            throw new MessagingException(String.format("Unable to write line to output stream (%s):", ToolClassUtils.getName(out)), e);
        }
    }

    private static byte[] getBytes(String s) {
        char[] charArr = s.toCharArray();
        int i = charArr.length;
        byte[] byteArr = new byte[i];
        int j = 0;

        while (j < i) {
            byteArr[j] = (byte) charArr[j++];
        }

        return byteArr;
    }
}
