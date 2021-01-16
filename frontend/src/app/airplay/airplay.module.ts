import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AirplaySettingsComponent } from './airplay-settings/airplay-settings.component';
import { PlayerComponent } from './player/player.component';



@NgModule({
  declarations: [AirplaySettingsComponent, PlayerComponent],
  imports: [
    CommonModule
  ],
  exports: [AirplaySettingsComponent, PlayerComponent]
})
export class AirplayModule { }
