package io.rayd.backend.webradio.service

import io.rayd.backend.orNull
import io.rayd.backend.webradio.dto.CreateStationRequest
import io.rayd.backend.webradio.dto.UpdateStationRequest
import io.rayd.backend.webradio.model.WebRadioStation
import io.rayd.backend.webradio.repository.WebRadioStationRepository
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
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
    fun search(query: String): List<WebRadioStation>
    fun listFavorites(): List<WebRadioStation>

    // Explicitly insert or update station
    fun save(webRadioStation: WebRadioStation)
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
                logo = downloadLogo(request.logo),
                favorite = true
            )
        )
    }

    override fun save(newStation: WebRadioStation) {
        newStation.let { station ->
            val dbStation = repository.findById(station.id).orNull()
            if (dbStation != null)
                station.copy(favorite = dbStation.favorite)
            else
                station
        }.let { station ->
            if (station.logo != null)
                station.copy(logo = downloadLogo(station.logo))
            else
                station
        }.let { station ->
            repository.save(station)
        }
    }

    @Transactional
    override fun update(stationId: UUID, request: UpdateStationRequest): WebRadioStation {
        val station = repository.findById(stationId).orNull() ?: throw StationNotFoundException(stationId)
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
            if (request.favorite != null) {
                it.copy(favorite = request.favorite)
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
        return repository.findById(stationId).orNull() ?: throw StationNotFoundException(stationId)
    }

    override fun search(query: String): List<WebRadioStation> {
        return repository.findByNameContainsOrderByName(query, PageRequest.of(0, 10))
    }

    override fun listFavorites(): List<WebRadioStation> {
        return repository.findAllByFavoriteTrueOrderByName()
    }

    private fun downloadLogo(logo: String?): String? {
        if (logo == null) return null
        if (logo.startsWith("data:image/")) return logo
        if (!logo.startsWith("http")) throw DownloadLogoException(logo, "Invalid URL")
        try {
            val url = URL(logo)
            val image = ImageIO.read(url)
            val bos = ByteArrayOutputStream()
            ImageIO.write(image, "png", bos)
            return "data:image/png;base64,${Base64Utils.encodeToString(bos.toByteArray())}"
        } catch (e: IOException) {
            throw DownloadLogoException(logo, "IOException occurred", e)
        }
    }
}

sealed class RadioStationException(message: String, cause: Throwable? = null) : RuntimeException(message, cause)

class DownloadLogoException(
    val logoUrl: String,
    message: String,
    cause: Throwable? = null
) :
    RadioStationException("Unable to download Logo: $message", cause)

data class StationNotFoundException(
    val stationId: UUID
) : RadioStationException("Station not found, Station Id: $stationId")
