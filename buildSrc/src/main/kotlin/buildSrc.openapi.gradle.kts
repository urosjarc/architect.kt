interface OpenApiExtension {
    val outputDir: Property<String>
    val inputDir: Property<String>
}

class OpenApi : Plugin<Project> {

    val className = OpenApi::class.simpleName.toString()

    override fun apply(project: Project) {

        val extension = project.extensions.create<OpenApiExtension>(className)

        project.task(className) {

            this.group = "buildSrc"
            this.description = "Fixing OpenAPI schema so that responses returning application/json."

            doLast {
                val outputDir = File(this.project.projectDir, extension.outputDir.get()).canonicalFile
                val inputDir = File(this.project.projectDir, extension.inputDir.get()).canonicalFile
                logger.warn("\nOutput dir: $outputDir")
                logger.warn("Input dir: $inputDir")
                inputDir.listFiles { file -> file.isFile && file.extension == "yaml" }?.forEach {
                    val content = it.readText()
                        .replace("'*/*':", "'application/json':")
                        .replace(Regex(""""https://.*?""""), """"http://0.0.0.0:${System.getenv("PORT")}"""")
                    val distFile = "${outputDir.absolutePath}/${it.name}"
                    logger.warn("CREATING: $distFile")
                    File(distFile).writeText(content)
                }
            }

        }
    }
}

apply<OpenApi>()
