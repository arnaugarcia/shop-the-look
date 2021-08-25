import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyRoutingModule } from './company-routing.module';
import { CompanyDeleteDialogComponent } from '../../entities/company/delete/company-delete-dialog.component';
import { CompanyDetailComponent } from './pages/detail/company-detail.component';

@NgModule({
  declarations: [CompanyDeleteDialogComponent, CompanyDetailComponent],
  imports: [SharedModule, CompanyRoutingModule],
})
export class CompanyModule {}
