package gov.hhs.onc.dcdt.version;

import gov.hhs.onc.dcdt.test.impl.AbstractToolUnitTests;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.testng.Assert;
import org.testng.annotations.Test;

@Test(dependsOnGroups = { "dcdt.test.unit.utils.all" }, groups = { "dcdt.test.unit.version" })
public class ToolVersionUnitTests extends AbstractToolUnitTests {
    @Autowired
    @SuppressWarnings({ "SpringJavaAutowiringInspection" })
    private ToolVersion version;

    @Test
    public void testModules() {
        String moduleGroupId, moduleArtifactId, moduleProjectVersion;

        for (ToolModuleVersion moduleVersion : this.version.getModuleVersions()) {
            moduleGroupId = moduleVersion.getGroupId();
            moduleArtifactId = moduleVersion.getArtifactId();
            moduleProjectVersion = moduleVersion.getVersion();

            Assert.assertFalse(StringUtils.isBlank(moduleGroupId),
                String.format("Module (artifactId=%s, version=%s) does not have a group ID.", moduleArtifactId, moduleProjectVersion));
            Assert.assertFalse(StringUtils.isBlank(moduleArtifactId),
                String.format("Module (groupId=%s, version=%s) does not have an artifact ID.", moduleGroupId, moduleProjectVersion));
            Assert.assertFalse(StringUtils.isBlank(moduleProjectVersion),
                String.format("Module (groupId=%s, artifactId=%s) does not have a version.", moduleGroupId, moduleArtifactId));

            Assert.assertTrue(moduleVersion.hasBuildTimestamp(), String.format(
                "Module (groupId=%s, artifactId=%s, version=%s) does not have a build timestamp.", moduleGroupId, moduleArtifactId, moduleProjectVersion));
            Assert.assertTrue(moduleVersion.hasBuildTimestampString(), String
                .format("Module (groupId=%s, artifactId=%s, version=%s) does not have a build timestamp string.", moduleGroupId, moduleArtifactId,
                    moduleProjectVersion));

            if (moduleVersion.hasHgAuthor()) {
                Assert.assertTrue(moduleVersion.hasHgAuthorMail(), String.format(
                    "Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial author mail address.", moduleGroupId, moduleArtifactId,
                    moduleProjectVersion));
                // Assertion for Mercurial author person should not be performed since its not guarenteed to exist.
                Assert.assertTrue(moduleVersion.hasHgBranch(), String.format(
                    "Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial branch.", moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgDate(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial date.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgDateString(), String.format(
                    "Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial date string.", moduleGroupId, moduleArtifactId,
                    moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgNode(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial node.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgNodeShort(), String.format(
                    "Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial short node.", moduleGroupId, moduleArtifactId,
                    moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgPath(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial path.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgRevision(), String
                    .format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial revision.", moduleGroupId, moduleArtifactId,
                        moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasHgTag(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Mercurial tag.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
            }
        }
    }
}
