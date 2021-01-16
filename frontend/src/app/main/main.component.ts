import { Component } from '@angular/core';

@Component({
  selector: 'app-root',
  templateUrl: './main.component.html',
  styleUrls: ['./main.component.scss']
})
export class AppComponent {
  menuItems = [
    {
      title: "Webradio",
      link: "/webradio"
    },
    {
      title: "Airplay",
      link: "/airplay"
    },
    {
      title: "Settings",
      link: "/settings"
    },
  ]
  isMenuCollapsed = true;
}
