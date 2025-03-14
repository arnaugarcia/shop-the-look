import { AccountService } from '../../core/auth/account.service';
import { Route, ROUTES, Routes } from '@angular/router';
import { CompanyListComponent } from './pages/list/company-list.component';
import { CompanyComponent } from './pages/company/company.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { BillingComponent } from './pages/billing/billing.component';
import { NgModule } from '@angular/core';
import { CompanyModule } from './company.module';
import { BillingAddressRoutingResolveService } from './route/billing-address-routing-resolve.service';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';
import { CompanyRoutingResolveService } from './route/company-routing-resolve.service';
import { PreferencesComponent } from './pages/preferences/preferences.component';
import { PreferencesResolveService } from './route/preferences-resolve.service';
import { SubscriptionRoutingResolveService } from './route/subscription-routing-resolve.service';
import { CheckoutSuccessComponent } from './pages/checkout/checkout-success/checkout-success.component';
import { CheckoutErrorComponent } from './pages/checkout/checkout-error/checkout-error.component';
import { ISubscriptionPlan } from './model/subscription-plan.model';

export interface RouteData {
  subscriptions: ISubscriptionPlan[];
}

interface SubscriptionsRoute extends Route {
  data?: RouteData;
}

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

const SUBSCRIPTION_ROUTES: SubscriptionsRoute[] = [
  {
    path: 'subscription',
    component: SubscriptionComponent,
    resolve: {
      subscriptions: SubscriptionRoutingResolveService,
    },
  },
  {
    path: 'subscription/success',
    component: CheckoutSuccessComponent,
    resolve: {
      subscriptions: SubscriptionRoutingResolveService,
    },
  },
  {
    path: 'subscription/error',
    component: CheckoutErrorComponent,
  },
];

export const COMPANY_CHILDREN_NAVIGATION = [
  {
    path: '',
    redirectTo: 'overview',
  },
  {
    path: 'overview',
    component: OverviewComponent,
    resolve: {
      company: CompanyRoutingResolveService,
    },
  },
  {
    path: 'preferences',
    component: PreferencesComponent,
    resolve: {
      preferences: PreferencesResolveService,
    },
  },
  ...SUBSCRIPTION_ROUTES,
  {
    path: 'billing',
    component: BillingComponent,
    resolve: {
      billingAddress: BillingAddressRoutingResolveService,
    },
  },
  {
    path: 'api-key',
    component: ApiKeyComponent,
    resolve: {
      company: CompanyRoutingResolveService,
    },
  },
];
