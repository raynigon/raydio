package player

type Player interface {
	Volume() int64
	VolumeUp()
	VolumeDown()

	Play()
	Stop()
	IsPlaying() bool

	ClearPlaylist()
	AddMedia(url string)

	Close()
}

// helper
func check(err error) {
	if err != nil {
		panic(err)
	}
}
