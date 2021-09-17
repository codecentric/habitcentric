package de.codecentric.hc.report.web.header.propagation

open class HeaderPropagationData(var headers: Map<String, String>?) {
    constructor(): this(emptyMap())
}
