import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyListComponent } from './pages/list/company-list.component';
import { CompaniesRoutingModule } from './companies-routing.module';

@NgModule({
  declarations: [CompanyListComponent],
  imports: [SharedModule, CompaniesRoutingModule],
})
export class CompaniesModule {}
