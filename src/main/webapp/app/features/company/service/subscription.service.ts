import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { ISubscriptionPlan } from '../model/subscription-plan.model';
import { ICheckoutData } from '../model/checkout.model';

export type EntityResponseType = HttpResponse<ISubscriptionPlan>;
export type EntityArrayResponseType = HttpResponse<ISubscriptionPlan[]>;

@Injectable({ providedIn: 'root' })
export class SubscriptionService {
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  queryForCompany(reference: string): Observable<EntityArrayResponseType> {
    return this.http.get<ISubscriptionPlan[]>(`/api/companies/${reference}/subscriptions`, { observe: 'response' });
  }

  queryForOwnCompany(): Observable<EntityArrayResponseType> {
    return this.http.get<ISubscriptionPlan[]>(`/api/company/subscriptions`, { observe: 'response' });
  }

  updateForCompany(reference: string, subscriptionReference: string): Observable<EntityResponseType> {
    return this.http.put<ISubscriptionPlan>(
      `/api/companies/${reference}/subscriptions`,
      { subscriptionReference: subscriptionReference },
      { observe: 'response' }
    );
  }

  checkoutSubscription(subscriptionReference: string): Observable<HttpResponse<ICheckoutData>> {
    return this.http.post<ICheckoutData>(`/api/company/subscriptions/${subscriptionReference}/checkout`, null, { observe: 'response' });
  }
}
