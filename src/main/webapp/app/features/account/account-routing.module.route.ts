import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { NgModule } from '@angular/core';
import { AccountComponent } from './pages/account/account.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';
import { CompanyComponent } from '../../entities/company/list/company.component';
import { PasswordComponent } from './pages/password/password.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';
import { BillingComponent } from './pages/billing/billing.component';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { EmployeesComponent } from './pages/employees/employees.component';

const accountRoute: Routes = [
  {
    path: '',
    component: AccountComponent,
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: '',
        redirectTo: 'settings',
      },
      {
        path: 'settings',
        component: SettingsComponent,
      },
      {
        path: 'company',
        component: CompanyComponent,
      },
      {
        path: 'billing',
        component: BillingComponent,
      },
      {
        path: 'subscription',
        component: SubscriptionComponent,
      },
      {
        path: 'employees',
        component: EmployeesComponent,
      },
      {
        path: 'password',
        component: PasswordComponent,
      },
      {
        path: 'notifications',
        component: NotificationsComponent,
      },
      {
        path: 'api-key',
        component: ApiKeyComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountRoute)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
