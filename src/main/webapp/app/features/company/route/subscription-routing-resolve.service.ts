import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, mergeMap } from 'rxjs/operators';
import { ISubscriptionPlan } from '../model/subscription-plan.model';
import { SubscriptionService } from '../service/subscription.service';

@Injectable({ providedIn: 'root' })
export class SubscriptionRoutingResolveService implements Resolve<ISubscriptionPlan[]> {
  constructor(protected service: SubscriptionService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubscriptionPlan[]> | Promise<ISubscriptionPlan[]> | ISubscriptionPlan[] {
    const reference = route.parent?.params.reference;
    if (reference) {
      return this.service.queryForCompany(reference).pipe(
        mergeMap(this.transformCompanySubscription()),
        catchError(() => of([]))
      );
    }
    return this.service.queryForOwnCompany().pipe(
      mergeMap(this.transformCompanySubscription()),
      catchError(() => of([]))
    );
  }

  private transformCompanySubscription() {
    return (response: HttpResponse<ISubscriptionPlan[]>) => {
      if (response.body) {
        return of(response.body);
      }
      return of([]);
    };
  }
}
