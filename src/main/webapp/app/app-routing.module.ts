import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { errorRoute } from './layouts/error/error.route';
import { Authority } from 'app/config/authority.constants';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { DocsComponent } from './admin/docs/docs.component';
import { HOME_ROUTE } from './home/home.route';

const LAYOUT_ROUTES = [...errorRoute];

@NgModule({
  imports: [
    RouterModule.forRoot([
      {
        path: 'admin',
        data: {
          authorities: [Authority.ADMIN],
        },
        canActivate: [UserRouteAccessService],
        loadChildren: () => import('./admin/admin-routing.module').then(m => m.AdminRoutingModule),
      },
      {
        path: 'api-docs',
        canActivate: [UserRouteAccessService],
        data: {
          animation: 'api-docs',
        },
        component: DocsComponent,
      },
      {
        path: 'auth',
        loadChildren: () => import('./auth/auth-routing.module').then(m => m.AuthRoutingModule),
      },
      HOME_ROUTE,
      ...LAYOUT_ROUTES,
    ]),
  ],
  exports: [RouterModule],
})
export class AppRoutingModule {}
