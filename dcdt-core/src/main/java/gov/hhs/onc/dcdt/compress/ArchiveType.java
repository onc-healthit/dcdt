package gov.hhs.onc.dcdt.compress;

import gov.hhs.onc.dcdt.net.mime.CoreContentTypes;
import org.apache.commons.compress.archivers.ArchiveEntry;
import org.apache.commons.compress.archivers.ArchiveStreamFactory;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.springframework.util.MimeType;

public enum ArchiveType {
    ZIP(ArchiveStreamFactory.ZIP, ".zip", new MimeType(CoreContentTypes.APP_TYPE, "zip"), ZipArchiveEntry.class);

    private final String type;
    private final String fileExt;
    private final MimeType contentType;
    private final Class<? extends ArchiveEntry> entryClass;

    private ArchiveType(String type, String fileExt, MimeType contentType, Class<? extends ArchiveEntry> entryClass) {
        this.type = type;
        this.fileExt = fileExt;
        this.contentType = contentType;
        this.entryClass = entryClass;
    }

    public MimeType getContentType() {
        return this.contentType;
    }

    public Class<? extends ArchiveEntry> getEntryClass() {
        return this.entryClass;
    }

    public String getFileExtension() {
        return this.fileExt;
    }

    public String getType() {
        return this.type;
    }
}
