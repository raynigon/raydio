import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { GeneralSettingsComponent } from './settings/general-settings/general-settings.component';
import { SettingsOverviewComponent } from './settings/settings-overview/settings-overview.component';
import { PlayerComponent as WebRadioPlayer } from './web-radio/player/player.component';
import { WebRadioSettingsComponent } from './web-radio/web-radio-settings/web-radio-settings.component';

const routes: Routes = [
  { path: 'webradio', component: WebRadioPlayer },
  { path: 'settings', component: SettingsOverviewComponent },
  { path: 'settings/general', component: GeneralSettingsComponent },
  { path: 'settings/webradio', component: WebRadioSettingsComponent },
  { path: '',   redirectTo: '/webradio', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
