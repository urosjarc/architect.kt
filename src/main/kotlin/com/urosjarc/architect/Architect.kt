package com.urosjarc.architect

import com.urosjarc.architect.core.DomainEntity
import com.urosjarc.architect.core.Repository
import com.urosjarc.architect.core.Service
import com.urosjarc.architect.core.UseCase
import io.github.classgraph.ClassGraph
import io.github.classgraph.ScanResult

public class Architect(private val packageName: String) {
    public val domainEntities: List<ArchitectClass>
    public val repos: List<ArchitectClass>
    public val services: List<ArchitectClass>
    public val useCases: List<ArchitectClass>

    private companion object {
        private fun classInfos(scanResult: ScanResult, anotation: Class<out Annotation>): List<ArchitectClass> {
            val classInfos = mutableListOf<ArchitectClass>()
            scanResult.getClassesWithAnnotation(anotation).forEach {
                val classInfo = ArchitectClass(kclass = it.name)
                classInfos.add(classInfo)
            }
            return classInfos
        }
    }

    init {
        ClassGraph()
            .verbose()
            .enableAllInfo()
            .acceptPackages(this.packageName)
            .scan().use { scanResult: ScanResult ->
                this.domainEntities = classInfos(scanResult, DomainEntity::class.java)
                this.repos = classInfos(scanResult, Repository::class.java)
                this.services = classInfos(scanResult, Service::class.java)
                this.useCases = classInfos(scanResult, UseCase::class.java)
            }
    }

}
