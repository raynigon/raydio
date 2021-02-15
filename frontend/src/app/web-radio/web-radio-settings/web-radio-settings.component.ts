import { Component, OnInit } from '@angular/core';
import { Observable } from 'rxjs';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  templateUrl: './web-radio-settings.component.html',
  styleUrls: ['./web-radio-settings.component.scss']
})
export class WebRadioSettingsComponent implements OnInit {

  public $stations: Observable<any>;
  public station = {
    name: '',
    stream: '',
    logo: ''
  };

  constructor(private repository: WebRadioRepositoryService) {
    this.$stations = null as any;
  }

  ngOnInit(): void {
    this.$stations = this.repository.listAllStations();
  }

  async addStation(): Promise<void> {
    await this.repository.addStation(this.station);
    this.station = {
      name: '',
      stream: '',
      logo: ''
    };
    this.$stations = this.repository.listAllStations();
  }

  async deleteStation(stationId: string): Promise<void> {
    await this.repository.deleteStation(stationId);
    this.$stations = this.repository.listAllStations();
  }

  async favorStation(station: any): Promise<void> {
    await this.repository.updateStation(station.id, {favorite: !station.favorite});
    this.$stations = this.repository.listAllStations();
  }
}
