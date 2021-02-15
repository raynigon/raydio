import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { AppStateService } from 'src/app/raydio/app-state/app-state.service';
import { WebRadioPlayerService } from '../web-radio-player/web-radio-player.service';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  public $stations: Observable<any>;
  public state: any;

  constructor(
    private playerService: WebRadioPlayerService,
    private repository: WebRadioRepositoryService,
    private appStateService: AppStateService
  ) {
    this.$stations = null as any;
  }

  ngOnInit(): void {
    this.$stations = this.repository.listStations();
    this.appStateService.$state.subscribe((event: any) => {
      this.state = event;
      console.log(event);
    });
  }

  async play(stationId: string): Promise<void> {
    try{
      await this.playerService.play(stationId);
    }catch (exception){
      console.error(exception);
    }
  }

  async stop(stationId: string): Promise<void> {
    try{
      await this.playerService.stop(stationId);
    }catch (exception){
      console.error(exception);
    }
  }

}
