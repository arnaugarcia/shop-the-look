import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { catchError, mergeMap } from 'rxjs/operators';

import { BillingAddressService } from '../service/billing-address.service';
import { BillingAddress, IBillingAddress } from '../model/billing-address.model';

@Injectable({ providedIn: 'root' })
export class BillingAddressRoutingResolveService implements Resolve<IBillingAddress> {
  constructor(protected service: BillingAddressService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IBillingAddress> | Promise<IBillingAddress> | IBillingAddress {
    const EMPTY = of(new BillingAddress());
    return this.service.query().pipe(
      mergeMap((response: HttpResponse<BillingAddress>) => {
        if (response.body) {
          return of(response.body);
        }
        return EMPTY;
      }),
      catchError(() => EMPTY)
    );
  }
}
