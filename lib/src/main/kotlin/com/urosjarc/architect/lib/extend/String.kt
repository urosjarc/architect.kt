package com.urosjarc.architect.lib.extend


internal val String.afterLastDot get() = this.split(".").last()
