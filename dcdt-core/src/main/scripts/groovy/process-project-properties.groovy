import org.apache.commons.lang3.BooleanUtils
import org.apache.commons.lang3.StringUtils
import org.apache.commons.lang3.time.FastDateFormat
import org.apache.maven.plugin.MojoExecutionException
import org.joda.time.DateTimeZone
import pl.project13.maven.git.GitCommitIdMojo
import pl.project13.maven.git.GitDescribeConfig
import pl.project13.maven.git.GitDirLocator
import pl.project13.maven.git.JGitProvider
import pl.project13.maven.git.log.MavenLoggerBridge

def String getExecutionProperty(String propName) {
    return this.getExecutionProperty(propName, null)
}

def String getExecutionProperty(String propName, String defaultPropValue) {
    return (project.properties.containsKey(propName) ? project.properties[propName] :
        (session.userProperties.containsKey(propName) ? session.userProperties[propName] :
        (session.systemProperties.containsKey(propName) ? session.systemProperties[propName] : defaultPropValue)))
}

def boolean containsExecutionProperty(String propName) {
    return (project.properties.containsKey(propName) || session.userProperties.containsKey(propName) || session.systemProperties.containsKey(propName))
}

project.properties["project.java.package.path"] = StringUtils.replace(project.properties["project.java.package"], ".", "/")

def skipTestsPropName = "skipTests"

if (!this.containsExecutionProperty(skipTestsPropName)) {
    project.properties[skipTestsPropName] = Boolean.toString(false)
}

def skipTestsPropValue = this.getExecutionProperty(skipTestsPropName)

[ "Unit", "Func", "It" ].each{
    def skipTestsTypePropName = (skipTestsPropName + it)
    
    if (!this.containsExecutionProperty(skipTestsTypePropName)) {
        project.properties[skipTestsTypePropName] = skipTestsPropValue
    }
    
    if (BooleanUtils.toBoolean(this.getExecutionProperty(skipTestsTypePropName))) {
        log.info("Tests of type (propName=${skipTestsTypePropName}) will be skipped.")
    }
}

def timestampFormatPattern = "yyyy-MM-dd HH:mm:ss Z"
def timestampTimeZoneId = DateTimeZone.default.ID
def timestampLocale = Locale.ROOT
def timestampFormat = FastDateFormat.getInstance(timestampFormatPattern, TimeZone.getTimeZone(timestampTimeZoneId), timestampLocale)

def gitDirLocator = new GitDirLocator(project, session.projects)
def gitProv = JGitProvider.on(gitDirLocator.lookupGitDirectory(new File("${project.properties["project.basedir.all"]}/.git")),
    new MavenLoggerBridge(log, false)).setAbbrevLength(7).setDateFormat(timestampFormatPattern).setDateFormatTimeZone(timestampTimeZoneId)
    .setGitDescribe(new GitDescribeConfig()).setPrefixDot(StringUtils.EMPTY)

def gitProps = new Properties()
gitProv.loadGitData(gitProps)

def modulePropsFile = new File(project.properties["project.build.modulePropertiesFile"])
def modulePropsDir = modulePropsFile.parentFile

if (!modulePropsDir.exists() && !modulePropsDir.mkdirs()) {
    throw new MojoExecutionException("Unable to make Maven module properties file parent directory tree: ${modulePropsDir.path}")
}

def modulePropNamePrefix = "dcdt.module.${project.artifactId}."
def moduleGitPropNamePrefix = "${modulePropNamePrefix}git."

modulePropsFile.write("""
#====================================================================================================
# MODULE
#====================================================================================================
${modulePropNamePrefix}groupId=${project.groupId}
${modulePropNamePrefix}artifactId=${project.artifactId}
${modulePropNamePrefix}version=${project.version}
${modulePropNamePrefix}name=${project.name}
${modulePropNamePrefix}description=${project.description}
${modulePropNamePrefix}url=${project.url}

#====================================================================================================
# MODULE BUILD
#====================================================================================================
${modulePropNamePrefix}build.timestamp=${timestampFormat.format(project.projectBuildingRequest.buildStartTime)}

#====================================================================================================
# MODULE GIT
#====================================================================================================
${moduleGitPropNamePrefix}author=${gitProps[GitCommitIdMojo.BUILD_AUTHOR_NAME]}
${moduleGitPropNamePrefix}branch=${gitProps[GitCommitIdMojo.BRANCH]}
${moduleGitPropNamePrefix}commit.id=${gitProps[GitCommitIdMojo.COMMIT_ID]}
${moduleGitPropNamePrefix}commit.id.short=${gitProps[GitCommitIdMojo.COMMIT_ID_ABBREV]}
${moduleGitPropNamePrefix}commit.timestamp=${gitProps[GitCommitIdMojo.COMMIT_TIME]}
${moduleGitPropNamePrefix}url=${gitProps[GitCommitIdMojo.REMOTE_ORIGIN_URL]}

""", project.properties["project.build.sourceEncoding"])
