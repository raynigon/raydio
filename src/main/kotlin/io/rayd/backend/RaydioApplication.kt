package io.rayd.backend

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.scheduling.annotation.EnableAsync
import java.io.BufferedInputStream
import java.io.IOException
import java.net.URL
import javax.sound.sampled.*
import javax.sound.sampled.AudioFormat.Encoding.PCM_SIGNED

@EnableAsync
@SpringBootApplication
class RaydioBackendApplication

fun main(args: Array<String>) {
    runApplication<RaydioBackendApplication>(*args)
}
