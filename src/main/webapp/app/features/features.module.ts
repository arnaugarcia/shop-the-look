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
        loadChildren: () => import('./companies/companies.module').then(m => m.CompaniesModule),
      },
      {
        path: 'company',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'account',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
      },
    ]),
  ],
})
export class FeaturesModule {}
