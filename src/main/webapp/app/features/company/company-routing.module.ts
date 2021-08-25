import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

const companyRoute: Routes = [
  {
    path: '',
    redirectTo: '/',
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyRoute)],
  exports: [RouterModule],
})
export class CompanyRoutingModule {}
