import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'employees',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./employee/employee.module').then(m => m.EmployeeModule),
      },
      {
        path: 'companies',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./company/company-routing.handler').then(m => m.CompanyRoutingHandler),
      },
      {
        path: 'company',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./company/company-routing.handler').then(m => m.CompanyRoutingHandler),
      },
      {
        path: 'spaces',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./space/space.module').then(m => m.SpaceModule),
      },
      {
        path: 'account',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
      },
      {
        path: 'analytics',
        loadChildren: () => import('./analytics/analytics.module').then(m => m.AnalyticsModule),
      },
    ]),
  ],
})
export class FeaturesModule {}
