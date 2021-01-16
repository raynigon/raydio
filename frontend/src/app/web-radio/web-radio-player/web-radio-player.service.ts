import { HttpClient } from '@angular/common/http';
import { Injectable } from '@angular/core';

@Injectable({
  providedIn: 'root'
})
export class WebRadioPlayerService {

  constructor(private http: HttpClient) { }

  public play(stationId: string): Promise<any> {
    return this.http.post(`/api/v1/webradio/${stationId}/play`, null)
      .toPromise()
  }

  public stop(stationId: string): Promise<any> {
    return this.http.post(`/api/v1/webradio/${stationId}/stop`, null)
      .toPromise()
  }
}
