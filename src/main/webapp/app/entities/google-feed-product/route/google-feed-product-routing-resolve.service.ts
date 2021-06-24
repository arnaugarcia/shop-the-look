import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { IGoogleFeedProduct, GoogleFeedProduct } from '../google-feed-product.model';
import { GoogleFeedProductService } from '../service/google-feed-product.service';

@Injectable({ providedIn: 'root' })
export class GoogleFeedProductRoutingResolveService implements Resolve<IGoogleFeedProduct> {
  constructor(protected service: GoogleFeedProductService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IGoogleFeedProduct> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((googleFeedProduct: HttpResponse<GoogleFeedProduct>) => {
          if (googleFeedProduct.body) {
            return of(googleFeedProduct.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new GoogleFeedProduct());
  }
}
