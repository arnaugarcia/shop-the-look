import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ISubscriptionPlan, getSubscriptionPlanIdentifier } from '../subscription-plan.model';

export type EntityResponseType = HttpResponse<ISubscriptionPlan>;
export type EntityArrayResponseType = HttpResponse<ISubscriptionPlan[]>;

@Injectable({ providedIn: 'root' })
export class SubscriptionPlanService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/subscription-plans');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(subscriptionPlan: ISubscriptionPlan): Observable<EntityResponseType> {
    return this.http.post<ISubscriptionPlan>(this.resourceUrl, subscriptionPlan, { observe: 'response' });
  }

  update(subscriptionPlan: ISubscriptionPlan): Observable<EntityResponseType> {
    return this.http.put<ISubscriptionPlan>(
      `${this.resourceUrl}/${getSubscriptionPlanIdentifier(subscriptionPlan) as number}`,
      subscriptionPlan,
      { observe: 'response' }
    );
  }

  partialUpdate(subscriptionPlan: ISubscriptionPlan): Observable<EntityResponseType> {
    return this.http.patch<ISubscriptionPlan>(
      `${this.resourceUrl}/${getSubscriptionPlanIdentifier(subscriptionPlan) as number}`,
      subscriptionPlan,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<ISubscriptionPlan>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ISubscriptionPlan[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addSubscriptionPlanToCollectionIfMissing(
    subscriptionPlanCollection: ISubscriptionPlan[],
    ...subscriptionPlansToCheck: (ISubscriptionPlan | null | undefined)[]
  ): ISubscriptionPlan[] {
    const subscriptionPlans: ISubscriptionPlan[] = subscriptionPlansToCheck.filter(isPresent);
    if (subscriptionPlans.length > 0) {
      const subscriptionPlanCollectionIdentifiers = subscriptionPlanCollection.map(
        subscriptionPlanItem => getSubscriptionPlanIdentifier(subscriptionPlanItem)!
      );
      const subscriptionPlansToAdd = subscriptionPlans.filter(subscriptionPlanItem => {
        const subscriptionPlanIdentifier = getSubscriptionPlanIdentifier(subscriptionPlanItem);
        if (subscriptionPlanIdentifier == null || subscriptionPlanCollectionIdentifiers.includes(subscriptionPlanIdentifier)) {
          return false;
        }
        subscriptionPlanCollectionIdentifiers.push(subscriptionPlanIdentifier);
        return true;
      });
      return [...subscriptionPlansToAdd, ...subscriptionPlanCollection];
    }
    return subscriptionPlanCollection;
  }
}
