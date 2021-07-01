import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { EMPTY, Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';

import { Company, ICompany } from '../company.model';
import { CompanyService } from '../service/company.service';

@Injectable({ providedIn: 'root' })
export class CompanyRoutingResolveService implements Resolve<ICompany> {
  constructor(protected service: CompanyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompany> | Observable<never> {
    const reference = route.params['id'];
    if (reference) {
      return this.service.find(reference).pipe(
        mergeMap((company: HttpResponse<Company>) => {
          if (company.body) {
            return of(company.body);
          } else {
            this.router.navigate(['404']);
            return EMPTY;
          }
        })
      );
    }
    return of(new Company());
  }
}
