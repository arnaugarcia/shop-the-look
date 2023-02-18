import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { ActivatedRouteSnapshot, Resolve, Router } from '@angular/router';
import { Observable, of } from 'rxjs';
import { mergeMap } from 'rxjs/operators';
import { PreferencesService } from '../service/preferences.service';
import { IPreferences } from '../model/preferences.model';

@Injectable({ providedIn: 'root' })
export class PreferencesResolveService implements Resolve<IPreferences> {
  constructor(protected service: PreferencesService, protected router: Router) {}

  resolve(route: ActivatedRouteSnapshot): Observable<IPreferences> | Promise<IPreferences> | IPreferences {
    const reference = route.parent?.params.reference;
    if (reference) {
      return this.service.find(reference).pipe(
        mergeMap((response: HttpResponse<IPreferences>) => {
          if (response.body) {
            return of(response.body);
          }
          this.router.navigate(['/404']);
          throw Error();
        })
      );
    }

    return this.service.query().pipe(
      mergeMap((response: HttpResponse<IPreferences>) => {
        if (response.body) {
          return of(response.body);
        }
        this.router.navigate(['/404']);
        throw Error();
      })
    );
  }
}
