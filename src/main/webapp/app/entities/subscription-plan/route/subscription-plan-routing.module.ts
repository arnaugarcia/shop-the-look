import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SubscriptionPlanComponent } from '../list/subscription-plan.component';
import { SubscriptionPlanDetailComponent } from '../detail/subscription-plan-detail.component';
import { SubscriptionPlanUpdateComponent } from '../update/subscription-plan-update.component';
import { SubscriptionPlanRoutingResolveService } from './subscription-plan-routing-resolve.service';

const subscriptionPlanRoute: Routes = [
  {
    path: '',
    component: SubscriptionPlanComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SubscriptionPlanDetailComponent,
    resolve: {
      subscriptionPlan: SubscriptionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SubscriptionPlanUpdateComponent,
    resolve: {
      subscriptionPlan: SubscriptionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SubscriptionPlanUpdateComponent,
    resolve: {
      subscriptionPlan: SubscriptionPlanRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(subscriptionPlanRoute)],
  exports: [RouterModule],
})
export class SubscriptionPlanRoutingModule {}
