import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlayerComponent } from './player/player.component';
import { WebRadioRepositoryService } from './web-radio-repository/web-radio-repository.service';
import { HttpClientModule } from '@angular/common/http';
import { WebRadioPlayerService } from './web-radio-player/web-radio-player.service';
import { FormsModule } from '@angular/forms';
import { MatTabsModule } from '@angular/material/tabs';
import { MatCardModule } from '@angular/material/card';
import { MatIconModule } from '@angular/material/icon';
import { MatButtonModule } from '@angular/material/button';
import { MatInputModule } from '@angular/material/input';
import { MatProgressSpinnerModule } from '@angular/material/progress-spinner';
import { WebRadioSettingsComponent } from './web-radio-settings/web-radio-settings.component';
import { CreateStationComponent } from './create-station/create-station.component';
import { StationListComponent } from './station-list/station-list.component';
import { StationSearchComponent } from './station-search/station-search.component';
import { StationRefreshComponent } from './station-refresh/station-refresh.component';
import { MatProgressBarModule } from '@angular/material/progress-bar';
import { RaydioModule } from '../raydio/raydio.module';

@NgModule({
  declarations: [
    PlayerComponent,
    WebRadioSettingsComponent,
    CreateStationComponent,
    StationListComponent,
    StationSearchComponent,
    StationRefreshComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    RaydioModule,
    HttpClientModule,
    MatCardModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
    MatInputModule,
    MatProgressBarModule,
    MatProgressSpinnerModule,
  ],
  exports: [
    PlayerComponent,
    WebRadioSettingsComponent,
    CreateStationComponent,
    StationListComponent,
    StationSearchComponent,
    StationRefreshComponent
  ],
  providers: [
    WebRadioRepositoryService,
    WebRadioPlayerService
  ]
})
export class WebRadioModule { }
