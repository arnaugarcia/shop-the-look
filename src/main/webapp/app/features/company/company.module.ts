import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyRoutingModule } from './company-routing.module';
import { CompanyComponent } from './pages/list/company.component';
import { CompanyDeleteDialogComponent } from '../../entities/company/delete/company-delete-dialog.component';

@NgModule({
  declarations: [CompanyComponent, CompanyDeleteDialogComponent],
  imports: [SharedModule, CompanyRoutingModule],
})
export class CompanyModule {}
