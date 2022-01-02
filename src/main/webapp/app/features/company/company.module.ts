import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyListComponent } from './pages/list/company-list.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { CompanyComponent } from './pages/company/company.component';
import { BillingComponent } from './pages/billing/billing.component';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';
import { CompanyModalErrorComponent } from './component/company-modal-error/company-modal-error.component';
import { CompanyModalSuccessComponent } from './component/company-modal-success/company-modal-success.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { PricingPlanComponent } from './component/pricing-plan/pricing-plan.component';
import { PricingPlanBenefitsComponent } from './component/pricing-plan-benefits/pricing-plan-benefits.component';
import { CheckoutSuccessComponent } from './pages/checkout/checkout-success/checkout-success.component';
import { CheckoutErrorComponent } from './pages/checkout/checkout-error/checkout-error.component';
import { CardSnippetModule } from '../../../@core/components/card-snippet/card-snippet.module';

@NgModule({
  declarations: [
    CompanyListComponent,
    OverviewComponent,
    BillingComponent,
    CompanyComponent,
    SubscriptionComponent,
    ApiKeyComponent,
    CompanyModalErrorComponent,
    CompanyModalSuccessComponent,
    PreferencesComponent,
    PricingPlanComponent,
    PricingPlanBenefitsComponent,
    CheckoutSuccessComponent,
    CheckoutErrorComponent,
  ],
  imports: [SharedModule, ContentHeaderModule, CardSnippetModule],
})
export class CompanyModule {}
