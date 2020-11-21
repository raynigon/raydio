package webradio

import (
	"fmt"

	"github.com/raynigon/raydio/pkg/player"
)

type SimpleWebradio struct {
	media    player.Player
	stations []RadioStation
	active   RadioStation
}

func CreateSimpleWebradio(media player.Player) *SimpleWebradio {
	stations := []RadioStation{
		{Id: "1", Name: "1Live", Url: "https://1liveuni-lh.akamaihd.net/i/1LIVE_HDS@179577/index_1_a-p.m3u8"},
		{Id: "2", Name: "Hit Radio N1", Url: "https://hitradion1-ais-edge-3106-fra-eco-cdn.cast.addradio.de/hitradion1/live/mp3/high/stream.mp3"},
	}
	radio := SimpleWebradio{media: media, stations: stations, active: stations[0]}
	radio.media.AddMedia(radio.active.Url)
	return &radio
}

func (radio *SimpleWebradio) CurrentStation() RadioStation {
	return radio.active
}

func (radio *SimpleWebradio) ListStation() []RadioStation {
	return radio.stations
}

func (radio *SimpleWebradio) SelectStation(id string) {
	if radio.active.Id == id {
		return
	}
	found := false
	for _, station := range radio.stations {
		if station.Id != id {
			continue
		}
		found = true
		radio.active = station
	}
	if !found {
		panic(fmt.Sprintf("Unable to find station with id: %s", id))
	}
	if radio.media.IsPlaying() {
		radio.media.Stop()
	}
	radio.media.ClearPlaylist()
	radio.media.AddMedia(radio.active.Url)
}

func (radio *SimpleWebradio) GetPlayer() player.Player {
	return radio.media
}
