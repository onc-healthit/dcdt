package gov.hhs.onc.dcdt.utils;


import com.fasterxml.jackson.databind.ObjectMapper;
import gov.hhs.onc.dcdt.json.ToolJsonException;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringWriter;
import java.io.Writer;

public abstract class ToolJsonUtils {
    public static <T> T fromJson(ObjectMapper objMapper, String str, Class<T> objClass) throws ToolJsonException {
        try {
            return objMapper.readValue(str, objClass);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to read JSON as object (class=%s):\n%s", ToolClassUtils.getName(objClass), str), e);
        }
    }

    public static <T> T fromJson(ObjectMapper objMapper, InputStream inStream, Class<T> objClass) throws ToolJsonException {
        try {
            return objMapper.readValue(inStream, objClass);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to read (inStreamClass=%s) JSON as object (class=%s).", ToolClassUtils.getName(inStream),
                ToolClassUtils.getName(objClass)), e);
        }
    }

    public static <T> T fromJson(ObjectMapper objMapper, Reader reader, Class<T> objClass) throws ToolJsonException {
        try {
            return objMapper.readValue(reader, objClass);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to read (readerClass=%s) JSON as object (class=%s).", ToolClassUtils.getName(reader),
                ToolClassUtils.getName(objClass)), e);
        }
    }

    public static <T> String toJson(ObjectMapper objMapper, T obj) throws ToolJsonException {
        try {
            objMapper.writeValueAsString(obj);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to write JSON for object (class=%s).", ToolClassUtils.getName(obj)), e);
        }

        StringWriter strWriter = new StringWriter();

        toJson(objMapper, strWriter, obj);

        return strWriter.toString();
    }

    public static <T> void toJson(ObjectMapper objMapper, OutputStream outStream, T obj) throws ToolJsonException {
        try {
            objMapper.writeValue(outStream, obj);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to write (outStreamClass=%s) JSON for object (class=%s).", ToolClassUtils.getName(outStream),
                ToolClassUtils.getName(obj)), e);
        }
    }

    public static <T> void toJson(ObjectMapper objMapper, Writer writer, T obj) throws ToolJsonException {
        try {
            objMapper.writeValue(writer, obj);
        } catch (IOException e) {
            throw new ToolJsonException(String.format("Unable to write (writerClass=%s) JSON for object (class=%s).", ToolClassUtils.getName(writer),
                ToolClassUtils.getName(obj)), e);
        }
    }
}
