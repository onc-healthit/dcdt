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
            Assert.assertTrue(moduleVersion.hasBuildTimestampString(),
                String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a build timestamp string.", moduleGroupId, moduleArtifactId,
                    moduleProjectVersion));

            if (moduleVersion.hasGitAuthor()) {
                Assert.assertTrue(moduleVersion.hasGitBranch(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git branch.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasGitCommitId(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git commit ID.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasGitCommitIdShort(),
                    String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git short commit ID.", moduleGroupId, moduleArtifactId,
                        moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasGitCommitTimestamp(),
                    String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git commit timestamp.", moduleGroupId, moduleArtifactId,
                        moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasGitCommitTimestampString(),
                    String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git commit timestamp string.", moduleGroupId,
                        moduleArtifactId, moduleProjectVersion));
                Assert.assertTrue(moduleVersion.hasGitUrl(), String.format("Module (groupId=%s, artifactId=%s, version=%s) does not have a Git URL.",
                    moduleGroupId, moduleArtifactId, moduleProjectVersion));
            }
        }
    }
}
