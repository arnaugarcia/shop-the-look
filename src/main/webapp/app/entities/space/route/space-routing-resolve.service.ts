import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpace, Space } from '../space.model';
import { SpaceService } from '../service/space.service';

@Injectable({ providedIn: 'root' })
export class SpaceRoutingResolveService implements Resolve<ISpace> {
  constructor(protected service: SpaceService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpace> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((space: HttpResponse<Space>) => {
          if (space.body) {
            return of(space.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Space());
  }
}
