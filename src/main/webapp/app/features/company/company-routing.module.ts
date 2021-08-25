import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyDetailComponent } from './pages/detail/company-detail.component';

const companyRoute: Routes = [
  {
    path: '',
    component: CompanyDetailComponent,
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
export class CompanyRoutingModule {}
