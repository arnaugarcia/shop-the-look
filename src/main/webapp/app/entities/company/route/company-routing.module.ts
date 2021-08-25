import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CompanyUpdateComponent } from '../update/company-update.component';
import { CompanyRoutingResolveService } from './company-routing-resolve.service';

const companyRoute: Routes = [
  {
    path: 'new',
    component: CompanyUpdateComponent,
    resolve: {
      company: CompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CompanyUpdateComponent,
    resolve: {
      company: CompanyRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(companyRoute)],
  exports: [RouterModule],
})
export class CompanyRoutingModule {}
