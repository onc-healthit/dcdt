package gov.hhs.onc.dcdt.compress.utils;

import gov.hhs.onc.dcdt.compress.ArchiveType;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.util.Map.Entry;
import javax.annotation.Nullable;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.ArchiveOutputStream;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.lang3.tuple.MutablePair;
import org.apache.commons.lang3.tuple.Pair;

public abstract class ArchiveUtils {
    @SuppressWarnings({ "unchecked" })
    public static <T extends ArchiveEntry> Pair<T, byte[]> transformZipArchiveEntryPair(Entry<String, byte[]> entryDescPair) {
        return new MutablePair<>((T) createEntry(entryDescPair), entryDescPair.getValue());
    }

    protected static ZipArchiveEntry createEntry(Entry<String, byte[]> entryDescPair) {
        ZipArchiveEntry entry = new ZipArchiveEntry(entryDescPair.getKey());
        entry.setSize(entryDescPair.getValue().length);

        return entry;
    }

    // @formatter:off
    /*
    @SuppressWarnings({ "unchecked" })
    public static <T extends ArchiveEntry> List<Pair<T, byte[]>> readArchive(ArchiveType type, InputStream inStream) throws ArchiveException {
        try (ArchiveInputStream archiveInStream = new ArchiveStreamFactory().createArchiveInputStream(type.getType(), inStream)) {
            List<Pair<T, byte[]>> entryPairs = new ArrayList<>();
            T entry;
            long entrySize;

            while ((entry = ((T) archiveInStream.getNextEntry())) != null) {
                if ((entrySize = entry.getSize()) == ArchiveEntry.SIZE_UNKNOWN) {
                    continue;
                }

                try {
                    entryPairs.add(new MutablePair<>(entry, IOUtils.toByteArray(archiveInStream, entrySize)));
                } catch (IOException e) {
                    throw new ArchiveException(String.format("Unable to read archive (type=%s) entry (name=%s, size=%d).", type.name(), entry.getName(),
                        entry.getSize()), e);
                }
            }

            return entryPairs;
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to close archive (type=%s) input stream.", type.name()), e);
        }
    }
    */
    // @formatter:on

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends ArchiveEntry> byte[] writeArchive(ArchiveType type, @Nullable Entry<T, byte[]> ... entryPairs) throws ArchiveException {
        return writeArchive(type, ToolArrayUtils.asList(entryPairs));
    }

    public static <T extends ArchiveEntry> byte[] writeArchive(ArchiveType type, @Nullable Iterable<? extends Entry<T, byte[]>> entryPairs)
        throws ArchiveException {
        try (ByteArrayOutputStream outStream = new ByteArrayOutputStream()) {
            writeArchive(type, outStream, entryPairs);
            outStream.flush();

            return outStream.toByteArray();
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to write archive (type=%s) data.", type.name()), e);
        }
    }

    @SafeVarargs
    @SuppressWarnings({ "varargs" })
    public static <T extends ArchiveEntry> void writeArchive(ArchiveType type, OutputStream outStream, @Nullable Entry<T, byte[]> ... entryPairs)
        throws ArchiveException {
        writeArchive(type, outStream, ToolArrayUtils.asList(entryPairs));
    }

    public static <T extends ArchiveEntry> void
        writeArchive(ArchiveType type, OutputStream outStream, @Nullable Iterable<? extends Entry<T, byte[]>> entryPairs) throws ArchiveException {
        try (ArchiveOutputStream archiveOutStream = new ArchiveStreamFactory().createArchiveOutputStream(type.getType(), outStream)) {
            if (entryPairs == null) {
                return;
            }

            for (Entry<T, byte[]> entryPair : entryPairs) {
                try {
                    archiveOutStream.putArchiveEntry(entryPair.getKey());
                    archiveOutStream.write(entryPair.getValue());
                    archiveOutStream.closeArchiveEntry();
                } catch (IOException e) {
                    throw new ArchiveException(String.format("Unable to write archive (type=%s) entry (name=%s, size=%d).", type.name(), entryPair.getKey()
                        .getName(), entryPair.getValue().length), e);
                }
            }

            archiveOutStream.finish();
        } catch (IOException e) {
            throw new ArchiveException(String.format("Unable to close archive (type=%s) output stream.", type.name()), e);
        }
    }
}
