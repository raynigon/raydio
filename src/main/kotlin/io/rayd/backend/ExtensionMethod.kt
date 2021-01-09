package io.rayd.backend

import java.util.Optional

fun <T> Optional<T>.orNull(): T? {
    return this.orElse(null)
}
