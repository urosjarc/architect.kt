package com.urosjarc.architect.test

import io.github.classgraph.ClassGraph

public annotation class Testing

@Testing
public class Test

fun main() {
    ClassGraph()
        .enableAllInfo()
        .verbose()
        .scan()
        .allClasses.forEach {
            println("!!!! $it")
        }
}
