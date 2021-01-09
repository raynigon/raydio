package io.rayd.backend.webradio.repository

import io.rayd.backend.webradio.model.WebRadioStation
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.stereotype.Repository
import java.util.UUID

@Repository
interface WebRadioStationRepository : JpaRepository<WebRadioStation, UUID>
