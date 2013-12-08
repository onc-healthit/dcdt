package gov.hhs.onc.dcdt.version.impl;

import gov.hhs.onc.dcdt.beans.impl.AbstractToolBean;
import gov.hhs.onc.dcdt.utils.ToolNumberUtils;
import gov.hhs.onc.dcdt.version.ToolModuleVersion;
import java.beans.ConstructorProperties;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.apache.commons.lang3.StringUtils;

public class ToolModuleVersionImpl extends AbstractToolBean implements ToolModuleVersion {
    private final static DateFormat BUILD_TIMESTAMP_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");
    private final static DateFormat HG_DATE_FORMAT = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss Z");

    private String groupId;
    private String artifactId;
    private String version;
    private String name;
    private String description;
    private Date buildTimestamp;
    private String hgAuthor;
    private String hgAuthorMail;
    private String hgAuthorPerson;
    private String hgBranch;
    private Date hgDate;
    private String hgNode;
    private String hgNodeShort;
    private String hgPath;
    private int hgRevision = -1;
    private String hgTag;

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

    @Override
    public Date getBuildTimestamp() {
        return this.buildTimestamp;
    }

    @Override
    public void setBuildTimestamp(Date buildTimestamp) {
        this.buildTimestamp = buildTimestamp;
    }

    @Override
    public boolean hasBuildTimestampString() {
        return this.getBuildTimestampString() != null;
    }

    @Override
    public String getBuildTimestampString() {
        return this.hasBuildTimestamp() ? BUILD_TIMESTAMP_DATE_FORMAT.format(this.buildTimestamp) : null;
    }

    @Override
    public void setBuildTimestampString(String buildTimestampStr) throws ParseException {
        this.buildTimestamp = BUILD_TIMESTAMP_DATE_FORMAT.parse(buildTimestampStr);
    }

    @Override
    public boolean hasHgAuthor() {
        return !StringUtils.isBlank(this.hgAuthor);
    }

    @Override
    public String getHgAuthor() {
        return this.hgAuthor;
    }

    @Override
    public void setHgAuthor(String hgAuthor) {
        this.hgAuthor = hgAuthor;
    }

    @Override
    public boolean hasHgAuthorMail() {
        return !StringUtils.isBlank(this.hgAuthorMail);
    }

    @Override
    public String getHgAuthorMail() {
        return this.hgAuthorMail;
    }

    @Override
    public void setHgAuthorMail(String hgAuthorMail) {
        this.hgAuthorMail = hgAuthorMail;
    }

    @Override
    public boolean hasHgAuthorPerson() {
        return !StringUtils.isBlank(this.hgAuthorPerson);
    }

    @Override
    public String getHgAuthorPerson() {
        return this.hgAuthorPerson;
    }

    @Override
    public void setHgAuthorPerson(String hgAuthorPerson) {
        this.hgAuthorPerson = hgAuthorPerson;
    }

    @Override
    public boolean hasHgBranch() {
        return !StringUtils.isBlank(this.hgBranch);
    }

    @Override
    public String getHgBranch() {
        return this.hgBranch;
    }

    @Override
    public void setHgBranch(String hgBranch) {
        this.hgBranch = hgBranch;
    }

    @Override
    public boolean hasHgDate() {
        return this.hgDate != null;
    }

    @Override
    public Date getHgDate() {
        return this.hgDate;
    }

    @Override
    public void setHgDate(Date hgDate) {
        this.hgDate = hgDate;
    }

    @Override
    public boolean hasHgDateString() {
        return this.getHgDateString() != null;
    }

    @Override
    public String getHgDateString() {
        return this.hasHgDate() ? HG_DATE_FORMAT.format(this.hgDate) : null;
    }

    @Override
    public void setHgDateString(String hgDateStr) throws ParseException {
        this.hgDate = HG_DATE_FORMAT.parse(hgDateStr);
    }

    @Override
    public boolean hasHgNode() {
        return !StringUtils.isBlank(this.hgNode);
    }

    @Override
    public String getHgNode() {
        return this.hgNode;
    }

    @Override
    public void setHgNode(String hgNode) {
        this.hgNode = hgNode;
    }

    @Override
    public boolean hasHgNodeShort() {
        return !StringUtils.isBlank(this.hgNodeShort);
    }

    @Override
    public String getHgNodeShort() {
        return this.hgNodeShort;
    }

    @Override
    public void setHgNodeShort(String hgNodeShort) {
        this.hgNodeShort = hgNodeShort;
    }

    @Override
    public boolean hasHgPath() {
        return !StringUtils.isBlank(this.hgPath);
    }

    @Override
    public String getHgPath() {
        return this.hgPath;
    }

    @Override
    public void setHgPath(String hgPath) {
        this.hgPath = hgPath;
    }

    @Override
    public boolean hasHgRevision() {
        return ToolNumberUtils.isPositive(this.hgRevision);
    }

    @Override
    public int getHgRevision() {
        return this.hgRevision;
    }

    @Override
    public void setHgRevision(int hgRevision) {
        this.hgRevision = hgRevision;
    }

    @Override
    public boolean hasHgTag() {
        return !StringUtils.isBlank(this.hgTag);
    }

    @Override
    public String getHgTag() {
        return this.hgTag;
    }

    @Override
    public void setHgTag(String hgTag) {
        this.hgTag = hgTag;
    }
}
