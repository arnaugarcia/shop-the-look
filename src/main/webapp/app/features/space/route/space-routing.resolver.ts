import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SpaceService } from '../service/space.service';
import { ISpace } from '../model/space.model';

@Injectable({ providedIn: 'root' })
export class SpaceRoutingResolver implements Resolve<ISpace> {
  constructor(protected service: SpaceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpace> | Observable<never> {
    const reference = route.params['space-reference'];
    if (reference) {
      return this.service.find(reference).pipe(
        mergeMap((space: HttpResponse<ISpace>) => {
          if (space.body) {
            return of(space.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    this.router.navigate(['404']);
    return EMPTY;
  }
}
