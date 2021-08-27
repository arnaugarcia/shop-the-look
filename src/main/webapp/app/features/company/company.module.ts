import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyDeleteDialogComponent } from '../../entities/company/delete/company-delete-dialog.component';
import { CompanyDetailComponent } from './pages/detail/company-detail.component';
import { CompanyListComponent } from './pages/list/company-list.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { CompanyComponent } from './pages/company/company.component';
import { BillingComponent } from './pages/billing/billing.component';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';
import { CompanyModalErrorComponent } from './component/company-modal-error/company-modal-error.component';
import { CompanyModalSuccessComponent } from './component/company-modal-success/company-modal-success.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';

@NgModule({
  declarations: [
    CompanyDeleteDialogComponent,
    CompanyDetailComponent,
    CompanyListComponent,
    OverviewComponent,
    BillingComponent,
    CompanyComponent,
    SubscriptionComponent,
    ApiKeyComponent,
    CompanyModalErrorComponent,
    CompanyModalSuccessComponent,
    PreferencesComponent,
  ],
  imports: [SharedModule],
})
export class CompanyModule {}
