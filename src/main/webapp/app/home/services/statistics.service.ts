import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from '../../core/config/application-config.service';
import { GeneralStatus, SubscriptionStatus } from '../models/statistics.model';

@Injectable({
  providedIn: 'root',
})
export class StatisticsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/stats');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  querySubscriptionStats(): Observable<HttpResponse<SubscriptionStatus>> {
    return this.http.get<SubscriptionStatus>(`${this.resourceUrl}/subscription`, { observe: 'response' });
  }

  queryGeneralStats(): Observable<HttpResponse<GeneralStatus>> {
    return this.http.get<GeneralStatus>(`${this.resourceUrl}/general`, { observe: 'response' });
  }
}
