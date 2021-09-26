import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SpaceReferenceGuard implements CanActivate {
  canActivate(route: ActivatedRouteSnapshot): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return (
      route.params.spaceReference !== 'template' && route.params.spaceReference !== 'customize' && route.params.spaceReference !== 'enjoy'
    );
  }
}
