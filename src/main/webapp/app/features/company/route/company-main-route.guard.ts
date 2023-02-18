import { Injectable } from '@angular/core';
import { ActivatedRouteSnapshot, CanActivate, Router, RouterStateSnapshot, UrlTree } from '@angular/router';
import { Observable } from 'rxjs';
import { AccountService } from '../../../core/auth/account.service';
import { StateStorageService } from '../../../core/auth/state-storage.service';
import { map } from 'rxjs/operators';
import { Authority } from '../../../config/authority.constants';
import { Account } from '../../../core/auth/account.model';

@Injectable({
  providedIn: 'root',
})
export class CompanyMainRouteGuard implements CanActivate {
  constructor(private router: Router, private accountService: AccountService, private stateStorageService: StateStorageService) {}

  canActivate(
    route: ActivatedRouteSnapshot,
    state: RouterStateSnapshot
  ): Observable<boolean | UrlTree> | Promise<boolean | UrlTree> | boolean | UrlTree {
    return this.accountService.identity().pipe(
      map(account => {
        if (account) {
          if (isAdmin(account) && state.url === '/companies') {
            return true;
          }
          return isManager(account) && state.url === '/companies';
        }

        this.stateStorageService.storeUrl(state.url);
        this.router.navigate(['/auth/login']);
        return false;
      })
    );

    function isAdmin(account: Account): boolean {
      return account.authorities.includes(Authority.ADMIN);
    }

    function isManager(account: Account): boolean {
      return account.authorities.includes(Authority.MANAGER);
    }
  }
}
