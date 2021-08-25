import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { NgModule } from '@angular/core';
import { AccountComponent } from './pages/account/account.component';
import { SettingsComponent } from './pages/settings/settings.component';
import { NotificationsComponent } from './pages/notifications/notifications.component';
import { PasswordComponent } from './pages/password/password.component';

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
        path: 'password',
        component: PasswordComponent,
      },
      {
        path: 'notifications',
        component: NotificationsComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountRoute)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
