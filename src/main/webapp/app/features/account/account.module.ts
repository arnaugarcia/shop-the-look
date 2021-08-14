import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AccountRoutingModule } from './account-routing.module.route';
import { SettingsComponent } from './pages/settings/settings.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';
import { AccountComponent } from './pages/account/account.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';

@NgModule({
  declarations: [SettingsComponent, PreferencesComponent, AccountComponent],
  imports: [SharedModule, ContentHeaderModule, AccountRoutingModule],
})
export class AccountModule {}
