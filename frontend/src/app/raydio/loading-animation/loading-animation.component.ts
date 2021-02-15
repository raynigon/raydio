import { Component, Input, OnInit } from '@angular/core';

@Component({
  selector: 'app-loading-animation',
  templateUrl: './loading-animation.component.html',
  styleUrls: ['./loading-animation.component.scss']
})
export class LoadingAnimationComponent implements OnInit {

  @Input()
  public size = 'big';

  public four: Array<number> = [0, 1, 2, 3];

  constructor() { }

  ngOnInit(): void {
  }

}
