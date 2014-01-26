package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.LdifException;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import gov.hhs.onc.dcdt.utils.ToolListUtils;
import gov.hhs.onc.dcdt.utils.ToolStringUtils;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.StringReader;
import java.io.StringWriter;
import java.io.Writer;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;

public abstract class ToolLdifUtils {
    public final static String DELIM_LDIF_ENTRY = "\n";

    public static LdifEntry readLdifEntry(String ldifEntryStr) throws LdifException {
        return ToolListUtils.getFirst(readLdifEntries(ldifEntryStr));
    }

    public static List<LdifEntry> readLdifEntries(String ldifEntriesStr) throws LdifException {
        return readLdifEntries(new StringReader(ldifEntriesStr));
    }

    public static List<LdifEntry> readLdifEntries(InputStream inStream) throws LdifException {
        return readLdifEntries(new InputStreamReader(inStream));
    }

    public static List<LdifEntry> readLdifEntries(Reader reader) throws LdifException {
        try (LdifReader ldifReader = new LdifReader(reader)) {
            return IteratorUtils.toList(ldifReader.iterator());
        } catch (IOException | LdapException e) {
            throw new LdifException(String.format("Unable to read LDIF from reader (class=%s).", ToolClassUtils.getName(reader)), e);
        }
    }

    public static String writeLdifEntries(LdifEntry ... ldifEntries) throws LdifException {
        return writeLdifEntries(ToolArrayUtils.asList(ldifEntries));
    }

    public static String writeLdifEntries(Iterable<LdifEntry> ldifEntries) throws LdifException {
        StringWriter writer = new StringWriter();

        writeLdifEntries(writer, ldifEntries);

        return writer.toString();
    }

    public static void writeLdifEntries(OutputStream outStream, LdifEntry ... ldifEntries) throws LdifException {
        writeLdifEntries(outStream, ToolArrayUtils.asList(ldifEntries));
    }

    public static void writeLdifEntries(OutputStream outStream, Iterable<LdifEntry> ldifEntries) throws LdifException {
        writeLdifEntries(new OutputStreamWriter(outStream), ldifEntries);
    }

    public static void writeLdifEntries(Writer writer, LdifEntry ... ldifEntries) throws LdifException {
        writeLdifEntries(writer, ToolArrayUtils.asList(ldifEntries));
    }

    public static void writeLdifEntries(Writer writer, Iterable<LdifEntry> ldifEntries) throws LdifException {
        try (BufferedWriter bufferedWriter = new BufferedWriter(writer)) {
            for (LdifEntry ldifEntry : ldifEntries) {
                bufferedWriter.write(LdifUtils.convertToLdif(ldifEntry));
                bufferedWriter.write(DELIM_LDIF_ENTRY);
            }
        } catch (IOException | LdapException e) {
            throw new LdifException(String.format("Unable to write LDIF to writer (class=%s).", ToolClassUtils.getName(writer)), e);
        }
    }

    public static String joinLdifEntryStrings(String ... ldifEntryStrs) {
        return joinLdifEntryStrings(ToolArrayUtils.asList(ldifEntryStrs));
    }

    public static String joinLdifEntryStrings(Iterable<String> ldifEntryStrs) {
        return ToolStringUtils.joinDelimit(ldifEntryStrs, DELIM_LDIF_ENTRY);
    }

    public static String[] splitLdifEntryString(String ldifEntryStr) {
        return StringUtils.split(ldifEntryStr, DELIM_LDIF_ENTRY);
    }
}
