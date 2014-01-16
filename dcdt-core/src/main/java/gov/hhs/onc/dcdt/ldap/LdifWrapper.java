package gov.hhs.onc.dcdt.ldap;

import java.io.BufferedWriter;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Reader;
import java.io.Writer;
import java.util.ArrayList;
import java.util.List;
import org.apache.directory.api.ldap.model.exception.LdapException;
import org.apache.directory.api.ldap.model.ldif.LdifEntry;
import org.apache.directory.api.ldap.model.ldif.LdifReader;
import org.apache.directory.api.ldap.model.ldif.LdifUtils;

public abstract class LdifWrapper {
    public static List<LdifEntry> readLdifEntries(byte[] data) throws LdifException {
        return readLdifEntries(new ByteArrayInputStream(data));
    }

    public static List<LdifEntry> readLdifEntries(InputStream inStream) throws LdifException {
        return readLdifEntries(new InputStreamReader(inStream));
    }

    public static List<LdifEntry> readLdifEntries(Reader reader) throws LdifException {
        try (LdifReader ldifReader = new LdifReader(reader)) {
            List<LdifEntry> ldifEntries = new ArrayList<>();
            while (ldifReader.hasNext()) {
                ldifEntries.add(ldifReader.next());
            }
            return ldifEntries;
        } catch (IOException | LdapException e) {
            throw new LdifException("Unable to parse LDIF entries from reader.", e);
        }
    }

    public static void writeLdifEntries(List<LdifEntry> ldifEntries, OutputStream outputStream) throws LdifException {
        writeLdifEntries(ldifEntries, new OutputStreamWriter(outputStream));
    }

    public static void writeLdifEntries(List<LdifEntry> ldifEntries, Writer writer) throws LdifException {
        try (BufferedWriter bw = new BufferedWriter(writer)) {
            for (LdifEntry ldifEntry : ldifEntries) {
                bw.write(LdifUtils.convertToLdif(ldifEntry));
                bw.newLine();
            }
        } catch (IOException | LdapException e) {
            throw new LdifException("Unable to write LDIF entries to writer.", e);
        }
    }

    public static byte[] writeLdifEntries(List<LdifEntry> ldifEntries) throws LdifException {
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        writeLdifEntries(ldifEntries, outStream);
        return outStream.toByteArray();
    }
}
