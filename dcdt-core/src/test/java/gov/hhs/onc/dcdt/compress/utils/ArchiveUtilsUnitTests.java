package gov.hhs.onc.dcdt.compress.utils;

import gov.hhs.onc.dcdt.compress.ArchiveType;
import gov.hhs.onc.dcdt.compress.utils.ArchiveUtils.ZipArchiveEntryPairTransformer;
import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import gov.hhs.onc.dcdt.utils.ToolArrayUtils;
import java.util.Collection;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.compress.archivers.zip.ZipArchiveEntry;
import org.apache.commons.lang3.tuple.ImmutablePair;
import org.apache.commons.lang3.tuple.Pair;
import org.springframework.beans.factory.annotation.Value;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(groups = { "dcdt.test.unit.compress.all", "dcdt.test.unit.compress.utils.all", "dcdt.test.unit.compress.utils.archive" })
public class ArchiveUtilsUnitTests extends AbstractToolUnitTests {
    @Value("${dcdt.test.archive.entry.1.name}")
    private String testArchiveEntry1Name;

    @Value("${dcdt.test.archive.entry.1.value}")
    private String testArchiveEntry1Value;

    @Value("${dcdt.test.archive.entry.2.name}")
    private String testArchiveEntry2Name;

    @Value("${dcdt.test.archive.entry.2.value}")
    private String testArchiveEntry2Value;

    private Collection<Pair<ZipArchiveEntry, byte[]>> testEntryPairs;
    private byte[] testArchiveData;

    // @formatter:off
    /*
    @Test(dependsOnMethods = { "testWriteArchive" })
    public void testReadArchive() throws ArchiveException {
        List<Pair<ZipArchiveEntry, byte[]>> testEntryPairsRead = ArchiveUtils.readArchive(ArchiveType.ZIP, new ByteArrayInputStream(this.testArchiveData));
        Pair<ZipArchiveEntry, byte[]> testEntryPair, testEntryPairRead;
        String testEntryName;

        Assert.assertEquals(testEntryPairsRead.size(), this.testEntryPairs.size(), "Number of archive entries does not match.");

        for (int a = 0; a < this.testEntryPairs.size(); a++) {
            Assert.assertEquals((testEntryPairRead = testEntryPairsRead.get(a)).getLeft().getName(),
                (testEntryName = (testEntryPair = this.testEntryPairs.get(a)).getLeft().getName()), "Archive entry names do not match.");
            Assert.assertEquals(testEntryPairRead.getRight(), testEntryPair.getRight(),
                String.format("Archive entry (name=%s) data does not match.", testEntryName));
        }
    }
    */
    // @formatter:on

    @SuppressWarnings({ "unchecked" })
    @Test
    public void testWriteArchive() throws ArchiveException {
        Assert
            .assertTrue(
                ((this.testArchiveData =
                    ArchiveUtils.writeArchive(
                        ArchiveType.ZIP,
                        (this.testEntryPairs =
                            CollectionUtils.collect(ToolArrayUtils.asList(
                                new ImmutablePair<>(this.testArchiveEntry1Name, this.testArchiveEntry1Value.getBytes()), new ImmutablePair<>(
                                    this.testArchiveEntry2Name, this.testArchiveEntry2Value.getBytes())), ZipArchiveEntryPairTransformer.INSTANCE)))).length > 0),
                "No archive data written.");
    }
}
