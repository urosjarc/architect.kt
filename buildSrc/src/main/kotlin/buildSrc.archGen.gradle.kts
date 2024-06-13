import io.github.classgraph.ClassGraph

interface ArchitectExtension {}

class Architect : Plugin<Project> {

    val className = Architect::class.simpleName.toString()

    override fun apply(project: Project) {

        project.task(className) {
            doLast {
                ClassGraph()
                    .acceptPaths("/home/username/project/lib/build/**")
                    .enableAllInfo()
                    .verbose()
                    .scan()
                    .allClasses.forEach {
                        println(it)
                    }
            }

        }
    }


}

apply<Architect>()
