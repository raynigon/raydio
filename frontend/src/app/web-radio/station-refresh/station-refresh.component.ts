import { Component, OnDestroy, OnInit } from '@angular/core';
import { Subscription } from 'rxjs';
import { AppStateService } from 'src/app/raydio/app-state/app-state.service';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-station-refresh',
  templateUrl: './station-refresh.component.html',
  styleUrls: ['./station-refresh.component.scss']
})
export class StationRefreshComponent implements OnInit, OnDestroy {

  public refreshing = false;

  private subscription!: Subscription;

  constructor(
    private repository: WebRadioRepositoryService,
    private appStateService: AppStateService
  ) { }

  ngOnInit(): void {
    this.subscription = this.appStateService.state$.subscribe((state) => this.handleState(state));
  }

  ngOnDestroy(): void {
    this.subscription.unsubscribe();
  }

  public refreshStations(): void {
    this.repository.refreshDirectory().catch(console.log);
  }

  private handleState(state: any): void {
    if (!state.hasOwnProperty('tasks')){
      this.refreshing = false;
      return;
    }
    console.log(state);
    const refreshTask = state.tasks.find((item: any) => item.name === 'refresh-station-directory');
    this.refreshing = refreshTask != null;
  }
}
