import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ICoordinate, Coordinate } from '../coordinate.model';
import { CoordinateService } from '../service/coordinate.service';

@Injectable({ providedIn: 'root' })
export class CoordinateRoutingResolveService implements Resolve<ICoordinate> {
  constructor(protected service: CoordinateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICoordinate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((coordinate: HttpResponse<Coordinate>) => {
          if (coordinate.body) {
            return of(coordinate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Coordinate());
  }
}
