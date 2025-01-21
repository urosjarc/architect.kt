package com.urosjarc.architect.lib

import org.apache.logging.log4j.kotlin.logger

internal class ClassMapping(classPackages: Map<String, String>) {

    /**
     * TODO: I can't get from parameters and their arguments package paths (this is bug from konsist)
     * for example if you have data class...
     *
     * data class Test(
     *      val test: Id<Employee>
     * )
     *
     * parameter test have type Id from which I can't get package path, and it has type argument Employee for which
     * I can't also get the package path, thats why I need association table...
     */
    private var className_to_package: MutableMap<String, String> = classPackages.toMutableMap()

    internal fun setPackage(className: String, packagePath: String) {
        if (this.className_to_package.contains(className)) {
            val firstImport = "${this.className_to_package[className]}.${className}"
            logger.warn("Duplicated class name: ${firstImport}, ${className}")
        }
        this.className_to_package[className] = packagePath
    }

    internal fun getPackage(className: String): String {
        return this.className_to_package[className] ?: throw Exception("Could not find package for class: '$className'")
    }

}
