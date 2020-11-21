package controller

import (
	"net/http"
	"strconv"

	"github.com/gin-gonic/gin"
	"github.com/raynigon/raydio/pkg/player"
)

// helper
func check(err error) {
	if err != nil {
		panic(err)
	}
}

func RegisterPlayerController(router *gin.Engine, play player.Player) {
	router.GET("/api/player", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"playing": play.IsPlaying(),
			"volume":  play.Volume(),
		})
	})
	router.PATCH("/api/player", func(c *gin.Context) {
		var patchRequest map[string]string
		c.BindJSON(&patchRequest)
		if volume, ok := patchRequest["volume"]; ok {
			value, err := strconv.ParseInt(volume, 10, 64)
			check(err)
			if value > play.Volume() {
				play.VolumeDown()
			} else if value < play.Volume() {
				play.VolumeDown()
			}
		}
		if playing, ok := patchRequest["playing"]; ok {
			value, err := strconv.ParseBool(playing)
			check(err)
			if value == true && play.IsPlaying() == false {
				play.Play()
			} else if value == false && play.IsPlaying() == true {
				play.Stop()
			}
		}
	})
}
