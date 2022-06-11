import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { ISpaceReport } from '../models/space-report.model';
import { IProductReport } from '../models/product-report.model';
import { IAnalyticsCriteria } from '../models/analytics-criteria.model';
import { createRequestOption } from '../../../core/request/request-util';

@Injectable({
  providedIn: 'root',
})
export class AnalyticsService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/analytics');

  constructor(private http: HttpClient, private applicationConfigService: ApplicationConfigService) {}

  findSpaceViews(criteria?: IAnalyticsCriteria): Observable<HttpResponse<ISpaceReport[]>> {
    const options = createRequestOption(criteria);
    return this.http.get<ISpaceReport[]>(`${this.resourceUrl}/spaces/views`, { observe: 'response', params: options });
  }

  findSpaceClicks(criteria?: IAnalyticsCriteria): Observable<HttpResponse<ISpaceReport[]>> {
    const options = createRequestOption(criteria);
    return this.http.get<ISpaceReport[]>(`${this.resourceUrl}/spaces/clicks`, { observe: 'response', params: options });
  }

  findProductClicks(criteria?: IAnalyticsCriteria): Observable<HttpResponse<IProductReport[]>> {
    const options = createRequestOption(criteria);
    return this.http.get<IProductReport[]>(`${this.resourceUrl}/products/clicks`, { observe: 'response', params: options });
  }

  findProductHovers(criteria?: IAnalyticsCriteria): Observable<HttpResponse<IProductReport[]>> {
    const options = createRequestOption(criteria);
    return this.http.get<IProductReport[]>(`${this.resourceUrl}/products/hovers`, { observe: 'response', params: options });
  }

  findSpaceViewsRelation(criteria?: IAnalyticsCriteria): Observable<HttpResponse<ISpaceReport[]>> {
    const options = createRequestOption(criteria);
    return this.http.get<ISpaceReport[]>(`${this.resourceUrl}/spaces/views/relation`, { observe: 'response', params: options });
  }
}
