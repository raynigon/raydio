import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';

@Injectable({
  providedIn: 'root'
})
export class WebRadioRepositoryService {

  constructor(private http: HttpClient) { }

  public listStations(): Observable<any> {
    return this.http.get("/api/v1/webradio/")
      .pipe(map(response => (response as any).items))
  }

  public listAllStations(): Observable<any> {
    return this.http.get("/api/v1/webradio/?favorites=false")
      .pipe(map(response => (response as any).items))
  }

  public addStation(station: any): Promise<any> {
    if (station.logo === ""){
      station.logo = null;
    }
    return this.http.post("/api/v1/webradio/", station).toPromise()
  }

  public updateStation(stationId: string, patch: any): Promise<any> {
    return this.http.patch(`/api/v1/webradio/${stationId}`, patch).toPromise()
  }

  public deleteStation(stationId: string): Promise<any> {
    return this.http.delete(`/api/v1/webradio/${stationId}`).toPromise()
  }
}
