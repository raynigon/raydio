package player

import (
	"bufio"
	"fmt"
	"io"
	"os/exec"
	"strconv"
	"strings"
	"sync"
)

type VlcPlayer struct {
	command *exec.Cmd
	in      io.WriteCloser
	out     io.ReadCloser
	results chan string
	mu      sync.Mutex
}

func CreateVlcPlayer() *VlcPlayer {
	command := exec.Command("vlc", "-I", "rc", "-q")
	inStream, err := command.StdinPipe()
	check(err)
	outStream, err := command.StdoutPipe()
	check(err)
	results := make(chan string, 100)

	go func() {
		reader := bufio.NewReader(outStream)
		for {
			data, err := reader.ReadString('>')
			if err != nil && data == "" {
				break
			}
			data = strings.ReplaceAll(data, "\n", "")
			data = strings.ReplaceAll(data, "\r", "")
			data = strings.ReplaceAll(data, ">", "")
			data = strings.TrimSpace(data)
			if data == "" {
				continue
			}
			if strings.Contains(data, "Command Line Interface initialized") {
				continue
			}
			results <- data
		}
	}()

	command.Start()

	player := VlcPlayer{
		command: command,
		in:      inStream,
		out:     outStream,
		results: results,
	}

	return &player
}

func (player *VlcPlayer) AddMedia(url string) {
	player.mu.Lock()
	player.action(fmt.Sprintf("add %s", url))
	player.mu.Unlock()
}

func (player *VlcPlayer) ClearPlaylist() {
	player.mu.Lock()
	player.action("clear")
	player.mu.Unlock()
}

func (player *VlcPlayer) Volume() int64 {
	player.mu.Lock()
	player.action("volume")
	result := <-player.results
	volume, err := strconv.ParseInt(result, 10, 64)
	check(err)
	player.mu.Unlock()
	return volume
}

func (player *VlcPlayer) IsPlaying() bool {
	player.mu.Lock()
	player.action("is_playing")
	result := <-player.results
	player.mu.Unlock()
	return result == "1"
}

func (player *VlcPlayer) VolumeUp() {
	volume := player.Volume()
	volume += 10
	if volume > 255 {
		volume = 255
	}
	player.mu.Lock()
	player.action(fmt.Sprintf("volume %d", volume))
	player.mu.Unlock()
}

func (player *VlcPlayer) VolumeDown() {
	volume := player.Volume()
	volume -= 10
	if volume < 0 {
		volume = 0
	}
	player.mu.Lock()
	player.action(fmt.Sprintf("volume %d", volume))
	player.mu.Unlock()
}

func (player *VlcPlayer) Play() {
	player.mu.Lock()
	player.action("play")
	player.mu.Unlock()
}

func (player *VlcPlayer) Stop() {
	player.mu.Lock()
	player.action("stop")
	player.mu.Unlock()
}

func (player *VlcPlayer) Close() {
	player.mu.Lock()
	player.action("quit")
	result := <-player.results
	if result != "Shutting down." {
		panic(fmt.Sprintf("Unexpected Command result: %s", result))
	}
	player.mu.Unlock()
	player.command.Wait()
}

func (player *VlcPlayer) action(command string) {
	command = fmt.Sprintf("%s\n", command)
	player.in.Write([]byte(command))
}
