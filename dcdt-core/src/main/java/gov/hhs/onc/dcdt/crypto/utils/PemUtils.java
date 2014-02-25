package gov.hhs.onc.dcdt.crypto.utils;

import gov.hhs.onc.dcdt.crypto.CryptographyException;
import gov.hhs.onc.dcdt.crypto.PemType;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.ByteArrayInputStream;
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
    public static byte[] readPemContent(byte[] data, String pemType) throws CryptographyException {
        return readPemObject(data, pemType).getContent();
    }

    public static PemObject readPemObject(byte[] data, String pemType) {
        return new PemObject(pemType, data);
    }

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
        try (PEMParser pemParser = new PEMParser(reader)) {
            return pemParser.readPemObject();
        } catch (IOException e) {
            throw new CryptographyException(String.format("Unable to read PEM object from reader (class=%s).", ToolClassUtils.getName(reader)), e);
        }
    }

    public static void writePemContent(OutputStream outStream, PemType pemType, byte[] pemContent) throws CryptographyException {
        writePemContent(new OutputStreamWriter(outStream), pemType, pemContent);
    }

    public static void writePemContent(Writer writer, PemType pemType, byte[] pemContent) throws CryptographyException {
        writePemObject(writer, new PemObject(pemType.getName(), pemContent));
    }

    public static void writePemObject(OutputStream outStream, PemObject pemObj) throws CryptographyException {
        writePemObject(new OutputStreamWriter(outStream), pemObj);
    }

    public static void writePemObject(Writer writer, PemObject pemObj) throws CryptographyException {
        try (PemWriter pemWriter = new PemWriter(writer)) {
            pemWriter.writeObject(pemObj);
            pemWriter.flush();
        } catch (IOException e) {
            throw new CryptographyException(String.format("Unable to write PEM object to writer (class=%s).", ToolClassUtils.getName(writer)), e);
        }
    }
}
