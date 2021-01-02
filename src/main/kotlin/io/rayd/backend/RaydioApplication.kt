package io.rayd.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RaydioBackendApplication

fun main(args: Array<String>) {
	runApplication<RaydioBackendApplication>(*args)
}
