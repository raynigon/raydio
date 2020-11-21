package controller

import (
	"net/http"

	"github.com/gin-gonic/gin"
	"github.com/raynigon/raydio/pkg/webradio"
)

func RegisterWebradioController(router *gin.Engine, radio webradio.Webradio) {
	router.GET("/api/webradio", func(c *gin.Context) {
		c.JSON(http.StatusOK, gin.H{
			"station": radio.CurrentStation(),
		})
	})
	router.PATCH("/api/webradio", func(c *gin.Context) {
		var patchRequest map[string]string
		c.BindJSON(&patchRequest)
		if stationId, ok := patchRequest["station"]; ok {
			radio.SelectStation(stationId)
		}
	})
}
