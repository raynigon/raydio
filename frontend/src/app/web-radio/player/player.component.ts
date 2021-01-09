import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { WebRadioPlayerService } from '../web-radio-player/web-radio-player.service';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-player',
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  public $stations: Observable<any>
  public station = {
    "name": "",
    "stream": "",
    "logo": ""
  }

  constructor(
    private playerService: WebRadioPlayerService,
    private repository: WebRadioRepositoryService
  ) { 
    this.$stations = null as any
  }

  ngOnInit(): void {
    this.$stations = this.repository.listStations()
  }

  play(stationId: string) {
    this.playerService.play(stationId)
  }

  addStation(){
    this.repository.addStation(this.station)
  }
}
