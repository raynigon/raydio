import { Injectable, OnDestroy } from '@angular/core';
import { BehaviorSubject, Observable } from 'rxjs';

@Injectable({
  providedIn: 'root'
})
export class AppStateService implements OnDestroy {

  public state$: BehaviorSubject<any>;
  private source!: EventSource;

  constructor() {
    this.state$ = new BehaviorSubject({
      source: null
    });
    this.init();
  }

  public handleEvent(event: any): void {
    this.state$.next(event);
  }

  public ngOnDestroy(): void {
    this.source.close();
  }

  private init(): void {
    this.source = new EventSource('/api/v1/application/state/events');
    this.source.addEventListener('application-state-update', (event: any) => {
      try{
        const content = JSON.parse(event.data);
        console.log('Application Event', content);
        this.handleEvent(content);
      }catch (exception){
        console.error(exception);
      }
    });
    this.source.onerror = (error) => {
      console.error('An error occured in the event stream', error);
      setTimeout(()=>this.init(), 10_000);
    };
  }
}
