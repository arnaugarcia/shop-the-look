import { RouterModule } from '@angular/router';
import { NgModule } from '@angular/core';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'login',
        loadChildren: () => import('./login/login.module').then(m => m.LoginModule),
      },
      {
        path: 'register',
        loadChildren: () => import('./register/register.module').then(m => m.RegisterModule),
      },
      {
        path: 'activate',
        loadChildren: () => import('./activate/activate.module').then(m => m.ActivateModule),
      },
      {
        path: 'init',
        loadChildren: () => import('./activate/activate.module').then(m => m.ActivateModule),
      },
      {
        path: 'reset',
        loadChildren: () => import('./password-reset/password-reset.module').then(m => m.PasswordResetModule),
      },
    ]),
  ],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
