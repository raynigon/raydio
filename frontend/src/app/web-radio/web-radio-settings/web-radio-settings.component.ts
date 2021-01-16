import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-web-radio-settings',
  templateUrl: './web-radio-settings.component.html',
  styleUrls: ['./web-radio-settings.component.scss']
})
export class WebRadioSettingsComponent implements OnInit {

  public $stations: Observable<any>
  public station = {
    "name": "",
    "stream": "",
    "logo": ""
  }

  constructor(private repository: WebRadioRepositoryService) {
    this.$stations = null as any;
  }

  ngOnInit(): void {
    this.$stations = this.repository.listStations();
  }

  async addStation() {
    await this.repository.addStation(this.station)
    this.station = {
      "name": "",
      "stream": "",
      "logo": ""
    };
    this.$stations = this.repository.listStations();
  }

  async deleteStation(stationId: string) {
    await this.repository.deleteStation(stationId);
    this.$stations = this.repository.listStations();
  }
}
