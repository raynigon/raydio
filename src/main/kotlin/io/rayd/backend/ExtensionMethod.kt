package io.rayd.backend

import liquibase.pro.packaged.T
import java.util.*

fun <T> Optional<T>.orNull(): T? {
    return this.orElse(null)
}