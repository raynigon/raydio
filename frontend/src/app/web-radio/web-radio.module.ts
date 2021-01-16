import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { PlayerComponent } from './player/player.component';
import { WebRadioRepositoryService } from './web-radio-repository/web-radio-repository.service';
import { HttpClientModule } from '@angular/common/http';
import { WebRadioPlayerService } from './web-radio-player/web-radio-player.service';
import { NgxBootstrapIconsModule, allIcons } from 'ngx-bootstrap-icons';
import { FormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [
    PlayerComponent
  ],
  imports: [
    CommonModule,
    FormsModule,
    HttpClientModule,
    NgbModule,
    NgxBootstrapIconsModule.pick(allIcons),
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
