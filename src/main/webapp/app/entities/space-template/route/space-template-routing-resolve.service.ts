import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, Router } from '@angular/router';
import { Observable, of, EMPTY } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { ISpaceTemplate, SpaceTemplate } from '../space-template.model';
import { SpaceTemplateService } from '../service/space-template.service';

@Injectable({ providedIn: 'root' })
export class SpaceTemplateRoutingResolveService implements Resolve<ISpaceTemplate> {
  constructor(protected service: SpaceTemplateService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ISpaceTemplate> | Observable<never> {
    const id = route.params['id'];
    if (id) {
      return this.service.find(id).pipe(
        mergeMap((spaceTemplate: HttpResponse<SpaceTemplate>) => {
          if (spaceTemplate.body) {
            return of(spaceTemplate.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new SpaceTemplate());
  }
}
