import gradle.kotlin.dsl.accessors._285dcef16d8875fee0ec91e18e07daf9.compileKotlin
import io.github.classgraph.ClassGraph

interface ArchitectExtension {}

//open class KotlinCompileTask : DefaultTask() {
//    @get:InputFilesinternal
//    val kotlinSources = project.fileTree(project.projectDir.resolve("src"))
//        .matching { incllde("**/*.kt") }
//}

class Architect : Plugin<Project> {

    val className = Architect::class.simpleName.toString()

    override fun apply(project: Project) {
        val srcFiles = project.fileTree(project.projectDir.resolve("src")).matching { include("**/*.kt") }
        val buildDir = project.layout.buildDirectory.asFile.get()
        val classPath = project.tasks.compileKotlin.get().destinationDirectory.asFile.get()

        project.task(className) {
            doLast {
                ClassGraph()
                    .overrideClasspath(classPath.absolutePath)
                    .enableAllInfo()
                    .verbose()
                    .scan()
                    .allClasses.forEach {
                        println("!!!! ${it.simpleName}")
                    }
            }

        }
    }


}

apply<Architect>()
