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

@NgModule({
  declarations: [
    PlayerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    MatTabsModule,
    MatCardModule,
    MatButtonModule,
    MatIconModule,
  ],
  exports: [
    PlayerComponent
  ],
  providers: [
    WebRadioRepositoryService,
    WebRadioPlayerService
  ]
})
export class WebRadioModule { }
