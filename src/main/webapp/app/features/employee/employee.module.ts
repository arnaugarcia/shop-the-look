import { NgModule } from '@angular/core';
import { EmployeeListComponent } from './pages/list/employee-list/employee-list.component';
import { SharedModule } from '../../shared/shared.module';
import { EmployeeRoutingModule } from './employee-routing.module';

@NgModule({
  imports: [SharedModule, EmployeeRoutingModule],
  declarations: [EmployeeListComponent],
})
export class EmployeeModule {}
