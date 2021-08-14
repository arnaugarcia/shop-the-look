import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';
import { Authority } from '../../config/authority.constants';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { EmployeeListComponent } from './pages/list/employee-list/employee-list.component';

const employeesRoute: Routes = [
  {
    path: '',
    component: EmployeeListComponent,
    data: {
      authorities: [Authority.ADMIN, Authority.MANAGER],
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(employeesRoute)],
  exports: [RouterModule],
})
export class EmployeeRoutingModule {}
