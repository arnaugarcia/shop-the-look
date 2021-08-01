import { Route } from '@angular/router';

import { PasswordResetInitComponent } from './password-reset-init.component';

export const passwordResetInitRoute: Route = {
  path: 'request',
  component: PasswordResetInitComponent,
  data: {
    pageTitle: 'global.menu.account.password',
  },
};
