import { Injectable } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppStateService {

  public $state: BehaviorSubject<any>
  private source: EventSource

  constructor() {
    this.$state = new BehaviorSubject({
      source: null
    });
    this.source = new EventSource('/api/v1/application/state/events');
    this.source.addEventListener("application-state-update", (event: any) => {
      try{
        const content = JSON.parse(event.data)
        this.handleEvent(content)
      }catch(exception){
        console.error(exception)
      }
    });
    this.source.onerror = (error) => this.$state.error(error)
  }

  public handleEvent(event: any) {
    this.$state.next(event)
  }
}
