import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { ISpaceReport } from '../models/space-report.model';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/analytics');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  findSpaceViews(): Observable<HttpResponse<ISpaceReport[]>> {
    return this.http.get<ISpaceReport[]>(`${this.resourceUrl}/spaces/views`, { observe: 'response' });
  }

  findSpaceClicks(): Observable<HttpResponse<ISpaceReport[]>> {
    return this.http.get<ISpaceReport[]>(`${this.resourceUrl}/spaces/clicks`, { observe: 'response' });
  }
}
