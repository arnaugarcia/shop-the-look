import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ISubscriptionPlan } from '../model/subscription-plan.model';

export type EntityResponseType = HttpResponse<ISubscriptionPlan>;
export type EntityArrayResponseType = HttpResponse<ISubscriptionPlan[]>;

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subscriptions');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(): Observable<EntityArrayResponseType> {
    return this.http.get<ISubscriptionPlan[]>(this.resourceUrl, { observe: 'response' });
  }
}
