import org.apache.maven.plugin.MojoExecutionException

[ project.properties["project.build.serviceLogDirectory"], project.properties["project.build.serviceVarDirectory"] ].each{
    def serviceDir = new File(it)
    
    if (!serviceDir.exists() && !serviceDir.mkdir()) {
        throw new MojoExecutionException("Unable to create service directory: ${it}")
    }
}

def serviceBinDir = new File(project.properties["project.build.serviceBinDirectory"])

ant.fileset(dir: serviceBinDir, includes: "wrapper-*, ${project.artifactId}, ${project.artifactId}.bat").each{
    if (!it.file.canExecute()) {
        it.file.setExecutable(true, false)
    }
}
