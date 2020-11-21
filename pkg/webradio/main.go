package webradio

import "github.com/raynigon/raydio/pkg/player"

type RadioStation struct {
	Id   string
	Name string
	Url  string
}

type Webradio interface {
	CurrentStation() RadioStation
	ListStation() []RadioStation
	SelectStation(id string)
	GetPlayer() player.Player
}
