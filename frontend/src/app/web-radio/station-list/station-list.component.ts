import { Component, EventEmitter, Input, OnChanges, OnInit, Output, SimpleChanges } from '@angular/core';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-station-list',
  templateUrl: './station-list.component.html',
  styleUrls: ['./station-list.component.scss']
})
export class StationListComponent implements OnInit, OnChanges {

  @Input()
  public favorites = true;

  @Input()
  public query = '';

  @Output()
  public favoritesChanged: EventEmitter<any>;

  public stations$!: Observable<any>;

  constructor(private repository: WebRadioRepositoryService) {
    this.favoritesChanged = new EventEmitter();
  }

  ngOnInit(): void {
    this.refresh().catch(console.log);
  }

  ngOnChanges(changes: SimpleChanges): void {
    this.refresh();
  }

  async refresh(): Promise<void> {
    if (this.favorites) {
      this.stations$ = this.repository.listStations();
    } else {
      this.stations$ = this.repository.searchStation(this.query);
    }
  }

  async favorStation(station: any): Promise<void> {
    await this.repository.updateStation(station.id, { favorite: !station.favorite });
    this.favoritesChanged.emit(station);
  }
}
