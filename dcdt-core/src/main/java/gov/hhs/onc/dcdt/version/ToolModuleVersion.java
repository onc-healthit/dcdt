package gov.hhs.onc.dcdt.version;

import gov.hhs.onc.dcdt.beans.ToolBean;
import java.text.ParseException;
import java.util.Date;
import javax.annotation.Nullable;

public interface ToolModuleVersion extends ToolBean {
    public String getGroupId();

    public String getArtifactId();

    public String getVersion();

    public String getName();

    public String getDescription();

    public boolean hasBuildTimestamp();

    @Nullable
    public Date getBuildTimestamp();

    public void setBuildTimestamp(@Nullable Date buildTimestamp);

    public boolean hasBuildTimestampString();

    @Nullable
    public String getBuildTimestampString();

    public void setBuildTimestampString(@Nullable String buildTimestampStr) throws ParseException;

    public boolean hasGitAuthor();

    @Nullable
    public String getGitAuthor();

    public void setGitAuthor(@Nullable String gitAuthor);

    public boolean hasGitBranch();

    @Nullable
    public String getGitBranch();

    public void setGitBranch(@Nullable String gitBranch);

    public boolean hasGitCommitId();

    @Nullable
    public String getGitCommitId();

    public void setGitCommitId(@Nullable String gitCommitId);

    public boolean hasGitCommitIdShort();

    @Nullable
    public String getGitCommitIdShort();

    public void setGitCommitIdShort(@Nullable String gitCommitIdShort);

    public boolean hasGitCommitTimestamp();

    @Nullable
    public Date getGitCommitTimestamp();

    public void setGitCommitTimestamp(@Nullable Date gitCommitTimestamp);

    public boolean hasGitCommitTimestampString();

    @Nullable
    public String getGitCommitTimestampString();

    public void setGitCommitTimestampString(@Nullable String gitCommitTimestampStr) throws ParseException;

    public boolean hasGitUrl();

    @Nullable
    public String getGitUrl();

    public void setGitUrl(@Nullable String gitUrl);
}
