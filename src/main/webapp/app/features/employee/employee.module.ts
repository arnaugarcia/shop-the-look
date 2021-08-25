import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { EmployeeRoutingModule } from './employee-routing.module';
import { CoreSidebarModule } from '../../../@core/components';
import { EmployeeAddSidebarComponent } from './components/employee-add-sidebar/employee-add-sidebar.component';
import { EmployeeDeleteDialogComponent } from './components/employee-delete/employee-delete-dialog.component';
import { EmployeeListComponent } from './pages/list/employee-list.component';

@NgModule({
  imports: [SharedModule, EmployeeRoutingModule, CoreSidebarModule],
  declarations: [EmployeeListComponent, EmployeeAddSidebarComponent, EmployeeDeleteDialogComponent],
})
export class EmployeeModule {}
