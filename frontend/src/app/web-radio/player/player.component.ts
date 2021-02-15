import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable, Subscription } from 'rxjs';
import { AppStateService } from 'src/app/raydio/app-state/app-state.service';
import { WebRadioPlayerService } from '../web-radio-player/web-radio-player.service';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  templateUrl: './player.component.html',
  styleUrls: ['./player.component.scss']
})
export class PlayerComponent implements OnInit {

  public stations$!: Observable<any>;
  public stations!: Array<any>;
  public state: any;
  public currentStationIndex: number | null = null;
  public activeRequest = false;

  private stationSubscription!: Subscription;
  private stateSubscription!: Subscription;

  constructor(
    private playerService: WebRadioPlayerService,
    private repository: WebRadioRepositoryService,
    private appStateService: AppStateService
  ) {}

  ngOnInit(): void {
    this.stations$ = this.repository.listStations();
    this.stationSubscription = this.stations$.subscribe((event: any) => {
      this.stations = event;
      this.update();
    });
    this.stateSubscription =this.appStateService.state$.subscribe((event: any) => {
      this.state = event;
      this.update();
    });
  }

  async play(stationId: string): Promise<void> {
    try{
      this.activeRequest = true;
      await this.playerService.play(stationId);
    }catch (exception){
      console.error(exception);
    }
    this.activeRequest = false;
  }

  async stop(stationId: string): Promise<void> {
    try{
      this.activeRequest = true;
      await this.playerService.stop(stationId);
    }catch (exception){
      console.error(exception);
    }
    this.activeRequest = false;
  }

  private update(): void {
    if(!this.stations) {return;}
    if(!this.state || !this.state.source) {return;}
    this.currentStationIndex = this.stations.findIndex(item => item.id === this.state.source.id);
  }
}
