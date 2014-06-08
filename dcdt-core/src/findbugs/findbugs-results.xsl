<?xml version="1.0" encoding="UTF-8"?>
<xsl:stylesheet
    version="2.0"
    xmlns:xsl="http://www.w3.org/1999/XSL/Transform">
    
    <xsl:output method="xml" encoding="UTF-8" indent="yes"/>
    
    <xsl:template name="bug">
        <xsl:param name="bugNode"/>
        <xsl:param name="bugIndex"/>
        <xsl:element name="bug{$bugIndex}">
            <category><xsl:value-of select="$bugNode/@category"/></category>
            <line><xsl:value-of select="$bugNode/@lineNumber"/></line>
            <msg><xsl:value-of select="$bugNode/@message"/></msg>
            <priority><xsl:value-of select="$bugNode/@priority"/></priority>
            <type><xsl:value-of select="$bugNode/@type"/></type>
        </xsl:element>
    </xsl:template>
    
    <xsl:template name="file">
        <xsl:param name="fileNode"/>
        <xsl:param name="fileIndex"/>
        <xsl:element name="file{$fileIndex}">
            <class><xsl:value-of select="$fileNode/@classname"/></class>
            <xsl:element name="bugs">
                <xsl:attribute name="num"><xsl:value-of select="count($fileNode/BugInstance)"/></xsl:attribute>
                <xsl:for-each select="$fileNode/BugInstance">
                    <xsl:call-template name="bug">
                        <xsl:with-param name="bugNode" select="current()"/>
                        <xsl:with-param name="bugIndex" select="position()"/>
                    </xsl:call-template>
                </xsl:for-each>
            </xsl:element>
        </xsl:element>
    </xsl:template>
    
    <xsl:template name="bugs">
        <xsl:param name="bugsNode"/>
        <effort><xsl:value-of select="$bugsNode/@effort"/></effort>
        <threshold><xsl:value-of select="$bugsNode/@threshold"/></threshold>
        <xsl:element name="files">
            <xsl:attribute name="num"><xsl:value-of select="count($bugsNode/file)"/></xsl:attribute>
            <xsl:for-each select="$bugsNode/file">
                <xsl:call-template name="file">
                    <xsl:with-param name="fileNode" select="current()"/>
                    <xsl:with-param name="fileIndex" select="position()"/>
                </xsl:call-template>
            </xsl:for-each>
        </xsl:element>
    </xsl:template>
    
    <xsl:template match="/">
        <findbugs>
            <results>
                <xsl:call-template name="bugs">
                    <xsl:with-param name="bugsNode" select="/BugCollection"/>
                </xsl:call-template>
            </results>
        </findbugs>
    </xsl:template>
</xsl:stylesheet>