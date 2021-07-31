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
    ]),
  ],
  exports: [RouterModule],
})
export class AuthRoutingModule {}
