package io.rayd.backend.webradio.service

import io.rayd.backend.orNull
import io.rayd.backend.webradio.dto.CreateStationRequest
import io.rayd.backend.webradio.dto.UpdateStationRequest
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.repository.WebRadioStationRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.util.Base64Utils
import java.io.ByteArrayOutputStream
import java.io.IOException
import java.net.URL
import java.util.UUID
import javax.imageio.ImageIO

interface WebRadioStationService {
    fun create(request: CreateStationRequest): WebRadioStation
    fun update(stationId: UUID, request: UpdateStationRequest): WebRadioStation
    fun delete(stationId: UUID)
    fun getDetails(stationId: UUID): WebRadioStation
    fun list(): List<WebRadioStation>
}

@Service
class DefaultWebRadioStationService(
    private val repository: WebRadioStationRepository
) : WebRadioStationService {

    override fun create(request: CreateStationRequest): WebRadioStation {
        return repository.save(
            WebRadioStation(
                id = UUID.randomUUID(),
                name = request.name,
                streamUrl = URL(request.stream),
                logo = downloadLogo(request.logo)
            )
        )
    }

    @Transactional
    override fun update(stationId: UUID, request: UpdateStationRequest): WebRadioStation {
        val station = repository.findById(stationId).orNull() ?: error("Station not found") // TODO change to exception
        return station.let {
            if (request.name != null) {
                it.copy(name = request.name)
            } else {
                it
            }
        }.let {
            if (request.stream != null) {
                it.copy(streamUrl = URL(request.stream))
            } else {
                it
            }
        }.let {
            if (request.logo != null) {
                it.copy(logo = downloadLogo(request.logo))
            } else {
                it
            }
        }.let {
            repository.save(it)
        }
    }

    override fun delete(stationId: UUID) {
        repository.deleteById(stationId)
    }

    override fun getDetails(stationId: UUID): WebRadioStation {
        return repository.findById(stationId).orNull() ?: error("Station not found") // TODO change to exception
    }

    override fun list(): List<WebRadioStation> {
        return repository.findAll()
    }

    private fun downloadLogo(logo: String?): String? {
        if (logo == null) return null
        if (logo.startsWith("data:image/")) return logo
        if (!logo.startsWith("http")) error("Invalid URL") // TODO change to exception
        try {
            val url = URL(logo)
            val image = ImageIO.read(url)
            val bos = ByteArrayOutputStream()
            ImageIO.write(image, "png", bos)
            return "data:image/png;base64,${Base64Utils.encodeToString(bos.toByteArray())}"
        } catch (e: IOException) {
            error("Unable to download Logo, $e") // TODO change to exception
        }
    }
}
