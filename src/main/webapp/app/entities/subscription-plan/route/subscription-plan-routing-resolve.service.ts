import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISubscriptionPlan, SubscriptionPlan } from '../subscription-plan.model';
import { SubscriptionPlanService } from '../service/subscription-plan.service';

@Injectable({ providedIn: 'root' })
export class SubscriptionPlanRoutingResolveService implements Resolve<ISubscriptionPlan> {
  constructor(protected service: SubscriptionPlanService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISubscriptionPlan> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((subscriptionPlan: HttpResponse<SubscriptionPlan>) => {
          if (subscriptionPlan.body) {
            return of(subscriptionPlan.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SubscriptionPlan());
  }
}
