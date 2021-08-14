import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AccountRoutingModule } from './account-routing.module.route';
import { SettingsComponent } from './pages/settings/settings.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';
import { AccountComponent } from './pages/account/account.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { NotificationsComponent } from './pages/notifications/notifications.component';
import { PasswordComponent } from './pages/password/password.component';
import { ApiKeyComponent } from './pages/api-key/api-key.component';
import { BillingComponent } from './pages/billing/billing.component';
import { SubscriptionComponent } from './pages/subscription/subscription.component';
import { EmployeesComponent } from './pages/employees/employees.component';

@NgModule({
  declarations: [
    SettingsComponent,
    PreferencesComponent,
    AccountComponent,
    NotificationsComponent,
    PasswordComponent,
    ApiKeyComponent,
    BillingComponent,
    SubscriptionComponent,
    EmployeesComponent,
  ],
  imports: [SharedModule, ContentHeaderModule, AccountRoutingModule],
})
export class AccountModule {}
