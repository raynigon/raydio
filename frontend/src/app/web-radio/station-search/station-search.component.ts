import { Component, EventEmitter, Input, OnInit, Output, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { StationListComponent } from '../station-list/station-list.component';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-station-search',
  templateUrl: './station-search.component.html',
  styleUrls: ['./station-search.component.scss']
})
export class StationSearchComponent implements OnInit {

  @ViewChild(StationListComponent)
  public stationList!: StationListComponent;

  @Output()
  public favoritesChanged: EventEmitter<any>;

  public stationName = '';

  constructor() {
    this.favoritesChanged = new EventEmitter();
  }

  ngOnInit(): void {}

  async refresh(): Promise<void> {
    this.stationList.refresh();
  }
}
