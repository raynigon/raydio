import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SettingsOverviewComponent } from './settings-overview/settings-overview.component';
import { RaydioModule } from '../raydio/raydio.module';
import { WebRadioModule } from '../web-radio/web-radio.module';
import {MatListModule} from '@angular/material/list';
import {MatDividerModule} from '@angular/material/divider';
import { RouterModule } from '@angular/router';
import { MatIconModule } from '@angular/material/icon';
import { GeneralSettingsComponent } from './general-settings/general-settings.component';
import { MatCardModule } from '@angular/material/card';


@NgModule({
  declarations: [SettingsOverviewComponent, GeneralSettingsComponent],
  imports: [
    CommonModule,
    RouterModule,
    MatIconModule,
    MatListModule,
    MatDividerModule,
    MatCardModule,
    RaydioModule,
    WebRadioModule
  ],
  exports: [SettingsOverviewComponent, GeneralSettingsComponent]
})
export class SettingsModule { }
