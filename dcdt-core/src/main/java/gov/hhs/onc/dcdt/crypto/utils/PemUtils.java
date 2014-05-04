package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import org.bouncycastle.openssl.PEMParser;
import org.bouncycastle.util.io.pem.PemObject;
import org.bouncycastle.util.io.pem.PemWriter;

public abstract class PemUtils {
    public static byte[] readPemContent(byte[] data) throws CryptographyException {
        return readPemContent(new ByteArrayInputStream(data));
    }

    public static byte[] readPemContent(InputStream inStream) throws CryptographyException {
        return readPemContent(new InputStreamReader(inStream));
    }

    public static byte[] readPemContent(Reader reader) throws CryptographyException {
        return readPemObject(reader).getContent();
    }

    public static PemObject readPemObject(byte[] data) throws CryptographyException {
        return readPemObject(new ByteArrayInputStream(data));
    }

    public static PemObject readPemObject(InputStream inStream) throws CryptographyException {
        return readPemObject(new InputStreamReader(inStream));
    }

    public static PemObject readPemObject(Reader reader) throws CryptographyException {
        try {
            return new PEMParser(reader).readPemObject();
        } catch (IOException e) {
            throw new CryptographyException(String.format("Unable to read PEM object from reader (class=%s).", ToolClassUtils.getName(reader)), e);
        }
    }

    public static byte[] writePemContent(PemType pemType, byte[] data) throws CryptographyException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();

        writePemContent(outStream, pemType, data);

        return outStream.toByteArray();
    }

    public static void writePemContent(OutputStream outStream, PemType pemType, byte[] data) throws CryptographyException {
        writePemContent(new OutputStreamWriter(outStream), pemType, data);
    }

    public static void writePemContent(Writer writer, PemType pemType, byte[] data) throws CryptographyException {
        writePemObject(writer, new PemObject(pemType.getId(), data));
    }

    public static void writePemObject(OutputStream outStream, PemObject pemObj) throws CryptographyException {
        writePemObject(new OutputStreamWriter(outStream), pemObj);
    }

    public static void writePemObject(Writer writer, PemObject pemObj) throws CryptographyException {
        PemWriter pemWriter = new PemWriter(writer);

        try {
            pemWriter.writeObject(pemObj);
            pemWriter.flush();
        } catch (IOException e) {
            throw new CryptographyException(String.format("Unable to write PEM object (type=%s) to writer (class=%s).", pemObj.getType(),
                ToolClassUtils.getName(writer)), e);
        }
    }
}
