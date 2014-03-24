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

    public boolean hasHgAuthor();

    @Nullable
    public String getHgAuthor();

    public void setHgAuthor(@Nullable String hgAuthor);

    public boolean hasHgAuthorMail();

    @Nullable
    public String getHgAuthorMail();

    public void setHgAuthorMail(@Nullable String hgAuthorMail);

    public boolean hasHgAuthorPerson();

    @Nullable
    public String getHgAuthorPerson();

    public void setHgAuthorPerson(@Nullable String hgAuthorPerson);

    public boolean hasHgBranch();

    @Nullable
    public String getHgBranch();

    public void setHgBranch(@Nullable String hgBranch);

    public boolean hasHgDate();

    @Nullable
    public Date getHgDate();

    public void setHgDate(@Nullable Date hgDate);

    public boolean hasHgDateString();

    @Nullable
    public String getHgDateString();

    public void setHgDateString(@Nullable String hgDateStr) throws ParseException;

    public boolean hasHgNode();

    @Nullable
    public String getHgNode();

    public void setHgNode(@Nullable String hgNode);

    public boolean hasHgNodeShort();

    @Nullable
    public String getHgNodeShort();

    public void setHgNodeShort(@Nullable String hgNodeShort);

    public boolean hasHgPath();

    @Nullable
    public String getHgPath();

    public void setHgPath(@Nullable String hgPath);

    public boolean hasHgRevision();

    public int getHgRevision();

    public void setHgRevision(int hgRevision);

    public boolean hasHgTag();

    @Nullable
    public String getHgTag();

    public void setHgTag(@Nullable String hgTag);
}
