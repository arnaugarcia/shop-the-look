import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyListComponent } from './pages/list/company-list.component';

const companyRoute: Routes = [
  {
    path: '',
    component: CompanyListComponent,
  },
  {
    path: ':reference',
    children: [
      {
        path: '',
        redirectTo: 'overview',
      },
      {
        path: 'overview',
      },
      {
        path: 'billing',
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyRoute)],
  exports: [RouterModule],
})
export class CompaniesRoutingModule {}
