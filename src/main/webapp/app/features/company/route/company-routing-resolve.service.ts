import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { ICompany } from '../model/company.model';
import { CompanyService } from '../service/company.service';

@Injectable({ providedIn: 'root' })
export class CompanyRoutingResolveService implements Resolve<ICompany> {
  constructor(protected service: CompanyService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<ICompany> | Promise<ICompany> | ICompany {
    const reference = route.parent?.params.reference;
    return this.service.find(reference).pipe(
      mergeMap((response: HttpResponse<ICompany>) => {
        if (response.body) {
          return of(response.body);
        }
        this.router.navigate(['/404']);
        throw Error();
      })
    );
  }
}
