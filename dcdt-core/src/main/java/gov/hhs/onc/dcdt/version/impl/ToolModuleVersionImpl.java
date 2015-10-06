package gov.hhs.onc.dcdt.version.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.version.ToolModuleVersion;
import java.beans.ConstructorProperties;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import javax.annotation.Nullable;
import org.apache.commons.lang3.StringUtils;

public class ToolModuleVersionImpl extends AbstractToolBean implements ToolModuleVersion {
    private final static DateFormat BUILD_TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final static DateFormat GIT_COMMIT_TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private Date buildTimestamp;
    private String gitAuthor;
    private String gitBranch;
    private Date gitCommitTimestamp;
    private String gitCommitId;
    private String gitCommitIdShort;
    private String gitUrl;

    @ConstructorProperties({ "groupId", "artifactId", "version", "name", "description" })
    public ToolModuleVersionImpl(String groupId, String artifactId, String version, String name, String description) {
        this.groupId = groupId;
        this.artifactId = artifactId;
        this.version = version;
        this.name = name;
        this.description = description;
    }

    @Override
    public String getGroupId() {
        return this.groupId;
    }

    @Override
    public String getArtifactId() {
        return this.artifactId;
    }

    @Override
    public String getVersion() {
        return this.version;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public String getDescription() {
        return this.description;
    }

    @Override
    public boolean hasBuildTimestamp() {
        return this.buildTimestamp != null;
    }

    @Nullable
    @Override
    public Date getBuildTimestamp() {
        return this.buildTimestamp;
    }

    @Override
    public void setBuildTimestamp(@Nullable Date buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
    }

    @Override
    public boolean hasBuildTimestampString() {
        return this.getBuildTimestampString() != null;
    }

    @Nullable
    @Override
    public String getBuildTimestampString() {
        return this.hasBuildTimestamp() ? BUILD_TIMESTAMP_DATE_FORMAT.format(this.buildTimestamp) : null;
    }

    @Override
    public void setBuildTimestampString(@Nullable String buildTimestampStr) throws ParseException {
        this.buildTimestamp = (buildTimestampStr != null) ? BUILD_TIMESTAMP_DATE_FORMAT.parse(buildTimestampStr) : null;
    }

    @Override
    public boolean hasGitAuthor() {
        return !StringUtils.isBlank(this.gitAuthor);
    }

    @Nullable
    @Override
    public String getGitAuthor() {
        return this.gitAuthor;
    }

    @Override
    public void setGitAuthor(@Nullable String hgAuthor) {
        this.gitAuthor = hgAuthor;
    }

    @Override
    public boolean hasGitBranch() {
        return !StringUtils.isBlank(this.gitBranch);
    }

    @Nullable
    @Override
    public String getGitBranch() {
        return this.gitBranch;
    }

    @Override
    public void setGitBranch(@Nullable String hgBranch) {
        this.gitBranch = hgBranch;
    }

    @Override
    public boolean hasGitCommitId() {
        return !StringUtils.isBlank(this.gitCommitId);
    }

    @Nullable
    @Override
    public String getGitCommitId() {
        return this.gitCommitId;
    }

    @Override
    public void setGitCommitId(@Nullable String hgNode) {
        this.gitCommitId = hgNode;
    }

    @Override
    public boolean hasGitCommitIdShort() {
        return !StringUtils.isBlank(this.gitCommitIdShort);
    }

    @Nullable
    @Override
    public String getGitCommitIdShort() {
        return this.gitCommitIdShort;
    }

    @Override
    public void setGitCommitIdShort(@Nullable String hgNodeShort) {
        this.gitCommitIdShort = hgNodeShort;
    }

    @Override
    public boolean hasGitCommitTimestamp() {
        return this.gitCommitTimestamp != null;
    }

    @Nullable
    @Override
    public Date getGitCommitTimestamp() {
        return this.gitCommitTimestamp;
    }

    @Override
    public void setGitCommitTimestamp(@Nullable Date hgDate) {
        this.gitCommitTimestamp = hgDate;
    }

    @Override
    public boolean hasGitCommitTimestampString() {
        return this.getGitCommitTimestampString() != null;
    }

    @Nullable
    @Override
    public String getGitCommitTimestampString() {
        return this.hasGitCommitTimestamp() ? GIT_COMMIT_TIMESTAMP_DATE_FORMAT.format(this.gitCommitTimestamp) : null;
    }

    @Override
    public void setGitCommitTimestampString(@Nullable String hgDateStr) throws ParseException {
        this.gitCommitTimestamp = (hgDateStr != null) ? GIT_COMMIT_TIMESTAMP_DATE_FORMAT.parse(hgDateStr) : null;
    }

    @Override
    public boolean hasGitUrl() {
        return !StringUtils.isBlank(this.gitUrl);
    }

    @Nullable
    @Override
    public String getGitUrl() {
        return this.gitUrl;
    }

    @Override
    public void setGitUrl(@Nullable String hgPath) {
        this.gitUrl = hgPath;
    }
}
