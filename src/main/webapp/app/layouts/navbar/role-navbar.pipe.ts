import { Pipe, PipeTransform } from '@angular/core';

@Pipe({
  name: 'stlRoleNavbar',
})
export class RoleNavbarPipe implements PipeTransform {
  private readonly TRANSLATE_KEY = 'global.menu.account.authorities.';

  transform(authorities: string[] | undefined): string {
    if (!authorities) {
      return this.TRANSLATE_KEY + 'empty';
    }

    if (authorities.includes('ROLE_ADMIN')) {
      return this.TRANSLATE_KEY + 'ROLE_ADMIN';
    } else if (authorities.includes('ROLE_MANAGER')) {
      return this.TRANSLATE_KEY + 'ROLE_MANAGER';
    } else {
      return this.TRANSLATE_KEY + 'ROLE_USER';
    }
  }
}
