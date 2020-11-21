package main

import (
	"time"

	"github.com/raynigon/raydio/pkg/player"
)

func main() {
	instance := player.CreateVlcPlayer()
	time.Sleep(time.Second)
	instance.Play()
	time.Sleep(5 * time.Second)
	instance.Stop()
	time.Sleep(5 * time.Second)
	instance.Close()
	time.Sleep(5 * time.Second)
}
