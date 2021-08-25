import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyDeleteDialogComponent } from '../../entities/company/delete/company-delete-dialog.component';
import { CompanyDetailComponent } from './pages/detail/company-detail.component';
import { CompanyListComponent } from './pages/list/company-list.component';
import { OverviewComponent } from './pages/overview/overview.component';
import { BillingComponent } from './pages/billing/billing.component';
import { CompanyComponent } from './pages/company/company.component';

@NgModule({
  declarations: [
    CompanyDeleteDialogComponent,
    CompanyDetailComponent,
    CompanyListComponent,
    OverviewComponent,
    BillingComponent,
    CompanyComponent,
  ],
  imports: [SharedModule],
})
export class CompanyModule {}
