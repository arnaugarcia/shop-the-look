import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { AccountRoutingModule } from './account-routing.module.route';
import { SettingsComponent } from './pages/settings/settings.component';
import { PreferencesComponent } from './pages/preferences/preferences.component';

@NgModule({
  declarations: [SettingsComponent, PreferencesComponent],
  imports: [SharedModule, AccountRoutingModule],
})
export class AccountModule {}
