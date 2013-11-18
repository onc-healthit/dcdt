package gov.hhs.onc.dcdt.version;


import gov.hhs.onc.dcdt.beans.ToolBean;
import java.text.ParseException;
import java.util.Date;

public interface ToolModuleVersion extends ToolBean {
    public String getGroupId();

    public String getArtifactId();

    public String getVersion();

    public String getName();

    public String getDescription();

    public boolean hasBuildTimestamp();

    public Date getBuildTimestamp();

    public void setBuildTimestamp(Date buildTimestamp);

    public boolean hasBuildTimestampString();

    public String getBuildTimestampString();

    public void setBuildTimestampString(String buildTimestampStr) throws ParseException;

    public boolean hasHgAuthor();

    public String getHgAuthor();

    public void setHgAuthor(String hgAuthor);

    public boolean hasHgAuthorMail();

    public String getHgAuthorMail();

    public void setHgAuthorMail(String hgAuthorMail);

    public boolean hasHgAuthorPerson();

    public String getHgAuthorPerson();

    public void setHgAuthorPerson(String hgAuthorPerson);

    public boolean hasHgBranch();

    public String getHgBranch();

    public void setHgBranch(String hgBranch);

    public boolean hasHgDate();

    public Date getHgDate();

    public void setHgDate(Date hgDate);

    public boolean hasHgDateString();

    public String getHgDateString();

    public void setHgDateString(String hgDateStr) throws ParseException;

    public boolean hasHgNode();

    public String getHgNode();

    public void setHgNode(String hgNode);

    public boolean hasHgNodeShort();

    public String getHgNodeShort();

    public void setHgNodeShort(String hgNodeShort);

    public boolean hasHgPath();

    public String getHgPath();

    public void setHgPath(String hgPath);

    public boolean hasHgRevision();

    public int getHgRevision();

    public void setHgRevision(int hgRevision);

    public boolean hasHgTag();

    public String getHgTag();

    public void setHgTag(String hgTag);
}
