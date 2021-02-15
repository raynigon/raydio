import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { Observable } from 'rxjs';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-station-search',
  templateUrl: './station-search.component.html',
  styleUrls: ['./station-search.component.scss']
})
export class StationSearchComponent implements OnInit {

  @Output()
  public favoritesChanged: EventEmitter<any>;

  public stationName = '';

  constructor() {
    this.favoritesChanged = new EventEmitter();
  }

  ngOnInit(): void {
  }

  async refresh(): Promise<void> {
    // TODO
  }
}
