import { AccountService } from '../../core/auth/account.service';
import { ROUTES, Routes } from '@angular/router';
import { CompanyListComponent } from './pages/list/company-list.component';
import { CompanyComponent } from './pages/company/company.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { BillingComponent } from './pages/billing/billing.component';
import { NgModule } from '@angular/core';
import { CompanyModule } from './company.module';
import { BillingAddressRoutingResolveService } from './route/billing-address-routing-resolve.service';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';

@NgModule({
  imports: [CompanyModule],
  providers: [
    {
      provide: ROUTES,
      useFactory: configBookHandlerRoutes,
      deps: [AccountService],
      multi: true,
    },
  ],
})
export class CompanyRoutingHandler {}

export function configBookHandlerRoutes(accountService: AccountService): Routes {
  let routes: Routes;
  if (accountService.isAdmin()) {
    routes = [
      {
        path: '',
        component: CompanyListComponent,
      },
      {
        path: ':reference',
        component: CompanyComponent,
        children: COMPANY_CHILDREN_NAVIGATION,
      },
    ];
  } else {
    routes = [
      {
        path: '',
        component: CompanyComponent,
        children: COMPANY_CHILDREN_NAVIGATION,
      },
    ];
  }
  return routes;
}

export const COMPANY_CHILDREN_NAVIGATION = [
  {
    path: '',
    redirectTo: 'overview',
  },
  {
    path: 'overview',
    component: OverviewComponent,
  },
  {
    path: 'billing',
    component: BillingComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
  },
  {
    path: 'subscription',
    component: SubscriptionComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
  },
  {
    path: 'api-key',
    component: ApiKeyComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
  },
];
