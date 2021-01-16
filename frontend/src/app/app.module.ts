import { BrowserModule } from '@angular/platform-browser';
import { NgModule } from '@angular/core';
import { HttpClientModule } from '@angular/common/http';
import { AppRoutingModule } from './app-routing.module';
import { AppComponent } from './main/main.component';
import { WebRadioModule } from './web-radio/web-radio.module';
import { BrowserAnimationsModule } from '@angular/platform-browser/animations';
import { MatToolbarModule } from '@angular/material/toolbar';
import { MatButtonModule } from '@angular/material/button';
import { MatIconModule } from '@angular/material/icon';
import { SettingsModule } from './settings/settings.module';
import { MatListModule } from '@angular/material/list';


@NgModule({
  declarations: [
    AppComponent
  ],
  imports: [
    BrowserModule,
    HttpClientModule,
    BrowserAnimationsModule,
    MatToolbarModule,
    MatButtonModule,
    MatIconModule,
    MatListModule,
    AppRoutingModule,
    WebRadioModule,
    SettingsModule,  
  ],
  providers: [],
  bootstrap: [AppComponent]
})
export class AppModule { }
