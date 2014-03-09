package gov.hhs.onc.dcdt.ldap.utils;

import gov.hhs.onc.dcdt.ldap.ToolLdapException;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import gov.hhs.onc.dcdt.utils.ToolClassUtils;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.Reader;
import java.io.StringReader;
import java.io.Writer;
import java.util.List;
import org.apache.commons.collections4.IteratorUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.text.StrBuilder;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;

public abstract class ToolLdifUtils {
    public static List<LdifEntry> readEntries(String str) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(new StringReader(str))) {
            return IteratorUtils.toList(ldifReader.iterator());
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entries from LDIF string:\n%s", str), e);
        }
    }

    public static List<LdifEntry> readEntries(InputStream inStream) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(inStream)) {
            return IteratorUtils.toList(ldifReader.iterator());
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entries from input stream (class=%s).", ToolClassUtils.getName(inStream)), e);
        }
    }

    public static List<LdifEntry> readEntries(Reader reader) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(reader)) {
            return IteratorUtils.toList(ldifReader.iterator());
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entries from reader (class=%s).", ToolClassUtils.getName(reader)), e);
        }
    }

    public static LdifEntry readEntry(String str) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(new StringReader(str))) {
            return ldifReader.next();
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entry from LDIF string:\n%s", str), e);
        }
    }

    public static LdifEntry readEntry(InputStream inStream) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(inStream)) {
            return ldifReader.next();
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entry from input stream (class=%s).", ToolClassUtils.getName(inStream)), e);
        }
    }

    public static LdifEntry readEntry(Reader reader) throws ToolLdapException {
        try (LdifReader ldifReader = new LdifReader(reader)) {
            return ldifReader.next();
        } catch (IOException | LdapException e) {
            throw new ToolLdapException(String.format("Unable to read LDIF entry from reader (class=%s).", ToolClassUtils.getName(reader)), e);
        }
    }

    public static void writeEntries(OutputStream outStream, LdifEntry ... ldifEntries) throws ToolLdapException {
        writeEntries(outStream, ToolArrayUtils.asList(ldifEntries));
    }

    public static void writeEntries(OutputStream outStream, Iterable<LdifEntry> ldifEntries) throws ToolLdapException {
        try {
            IOUtils.write(writeEntries(ldifEntries), outStream);
        } catch (IOException e) {
            throw new ToolLdapException(String.format("Unable to write LDIF entries to output stream (class=%s).", ToolClassUtils.getName(outStream)), e);
        }
    }

    public static void writeEntries(Writer writer, LdifEntry ... ldifEntries) throws ToolLdapException {
        writeEntries(writer, ToolArrayUtils.asList(ldifEntries));
    }

    public static void writeEntries(Writer writer, Iterable<LdifEntry> ldifEntries) throws ToolLdapException {
        try {
            IOUtils.write(writeEntries(ldifEntries), writer);
        } catch (IOException e) {
            throw new ToolLdapException(String.format("Unable to write LDIF entries to writer (class=%s).", ToolClassUtils.getName(writer)), e);
        }
    }

    public static String writeEntries(LdifEntry ... ldifEntries) throws ToolLdapException {
        return writeEntries(ToolArrayUtils.asList(ldifEntries));
    }

    public static String writeEntries(Iterable<LdifEntry> ldifEntries) throws ToolLdapException {
        StrBuilder ldifBuilder = new StrBuilder();

        for (LdifEntry ldifEntry : ldifEntries) {
            ldifBuilder.appendSeparator(StringUtils.LF);
            ldifBuilder.append(writeEntry(ldifEntry));
        }

        return ldifBuilder.build();
    }

    public static String writeEntry(LdifEntry ldifEntry) throws ToolLdapException {
        try {
            return LdifUtils.convertToLdif(ldifEntry);
        } catch (LdapException e) {
            throw new ToolLdapException(String.format("Unable to write LDIF entry (dn={%s}) to LDIF string.", ldifEntry.getDn().getName()), e);
        }
    }
}
