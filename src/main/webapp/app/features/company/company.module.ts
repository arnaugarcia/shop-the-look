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
  ],
  imports: [SharedModule],
})
export class CompanyModule {}
