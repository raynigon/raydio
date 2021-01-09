import { NgModule } from '@angular/core';
import { Routes, RouterModule } from '@angular/router';
import { PlayerComponent as WebRadioPlayer } from './web-radio/player/player.component';

const routes: Routes = [
  { path: 'webradio', component: WebRadioPlayer },
  { path: '',   redirectTo: '/webradio', pathMatch: 'full' },
];

@NgModule({
  imports: [RouterModule.forRoot(routes)],
  exports: [RouterModule]
})
export class AppRoutingModule { }
