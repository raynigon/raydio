import { Component, EventEmitter, OnInit, Output } from '@angular/core';
import { WebRadioRepositoryService } from '../web-radio-repository/web-radio-repository.service';

@Component({
  selector: 'wr-create-station',
  templateUrl: './create-station.component.html',
  styleUrls: ['./create-station.component.scss']
})
export class CreateStationComponent implements OnInit {

  @Output()
  public created: EventEmitter<any>;

  public station = {
    name: '',
    stream: '',
    logo: ''
  };

  constructor(private repository: WebRadioRepositoryService) {
    this.created = new EventEmitter();
  }

  ngOnInit(): void {
  }

  async addStation(): Promise<void> {
    await this.repository.addStation(this.station);
    this.created.emit(this.station);
    this.station = {
      name: '',
      stream: '',
      logo: ''
    };
  }
}
