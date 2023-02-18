import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AccountRoutingModule } from './account-routing.module.route';
import { SettingsComponent } from './pages/settings/settings.component';
import { AccountComponent } from './pages/account/account.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { PasswordComponent } from './pages/password/password.component';

@NgModule({
  declarations: [SettingsComponent, AccountComponent, PasswordComponent],
  imports: [SharedModule, ContentHeaderModule, AccountRoutingModule],
})
export class AccountModule {}
