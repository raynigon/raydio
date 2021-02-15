import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { AppStateService } from './app-state/app-state.service';
import { LoadingAnimationComponent } from './loading-animation/loading-animation.component';



@NgModule({
  declarations: [LoadingAnimationComponent],
  imports: [
    CommonModule
  ],
  providers: [
    AppStateService
  ],
  exports: [LoadingAnimationComponent]
})
export class RaydioModule { }
