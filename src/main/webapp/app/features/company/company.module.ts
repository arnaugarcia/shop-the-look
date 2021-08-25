import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { CompanyRoutingModule } from './company-routing.module';
import { CompanyDeleteDialogComponent } from '../../entities/company/delete/company-delete-dialog.component';
import { CompanyListComponent } from './pages/list/company-list.component';

@NgModule({
  declarations: [CompanyListComponent, CompanyDeleteDialogComponent],
  imports: [SharedModule, CompanyRoutingModule],
})
export class CompanyModule {}
