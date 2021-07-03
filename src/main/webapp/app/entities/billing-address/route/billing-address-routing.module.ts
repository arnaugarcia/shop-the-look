import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { BillingAddressComponent } from '../list/billing-address.component';
import { BillingAddressDetailComponent } from '../detail/billing-address-detail.component';
import { BillingAddressUpdateComponent } from '../update/billing-address-update.component';
import { BillingAddressRoutingResolveService } from './billing-address-routing-resolve.service';

const billingAddressRoute: Routes = [
  {
    path: '',
    component: BillingAddressComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: BillingAddressDetailComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: BillingAddressUpdateComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: BillingAddressUpdateComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(billingAddressRoute)],
  exports: [RouterModule],
})
export class BillingAddressRoutingModule {}
