import { Component, OnInit, ViewChild } from '@angular/core';
import { Observable } from 'rxjs';
import { StationListComponent } from '../station-list/station-list.component';
import { StationSearchComponent } from '../station-search/station-search.component';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  templateUrl: './web-radio-settings.component.html',
  styleUrls: ['./web-radio-settings.component.scss']
})
export class WebRadioSettingsComponent implements OnInit {

  @ViewChild(StationSearchComponent)
  public searchComponent!: StationSearchComponent;

  @ViewChild(StationListComponent)
  public favoritesComponent!: StationListComponent;


  constructor() {}


  ngOnInit(): void {}

  public async update(): Promise<void>{
    Promise.all([
      this.searchComponent.refresh(),
      this.favoritesComponent.refresh()
    ]).catch(console.log);
  }
}
