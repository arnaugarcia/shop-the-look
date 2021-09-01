import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { SpaceService } from '../service/space.service';
import { ISpace, Space } from '../model/space.model';

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
