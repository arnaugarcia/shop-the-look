import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { CompanyComponent } from './pages/list/company.component';

const companyRoute: Routes = [
  {
    path: '',
    component: CompanyComponent,
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyRoute)],
  exports: [RouterModule],
})
export class CompanyRoutingModule {}
