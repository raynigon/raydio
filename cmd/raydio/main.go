package main

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/raynigon/raydio/pkg/controller"
	"github.com/raynigon/raydio/pkg/player"
	"github.com/raynigon/raydio/pkg/webradio"
)

func main() {
	router := gin.Default()
	router.LoadHTMLGlob("resources/templates/*")
	playerInstance := player.CreateVlcPlayer()
	webradioInstance := webradio.CreateSimpleWebradio(playerInstance)

	router.GET("/", func(c *gin.Context) {
		c.HTML(http.StatusOK, "index.tmpl", gin.H{
			"service":  "webradio",
			"station":  webradioInstance.CurrentStation().Name,
			"volume":   playerInstance.Volume(),
			"playing":  playerInstance.IsPlaying(),
			"stations": webradioInstance.ListStation(),
		})
	})

	controller.RegisterPlayerController(router, playerInstance)
	controller.RegisterWebradioController(router, webradioInstance)

	router.Run() // listen and serve on 0.0.0.0:8080 (for windows "localhost:8080")
}
