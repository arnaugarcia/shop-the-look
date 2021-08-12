import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { SettingsComponent } from './settings/settings.component';
import { accountState } from './account.route';
import { PreferencesComponent } from './preferences/preferences.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild(accountState)],
  declarations: [SettingsComponent, PreferencesComponent],
})
export class AccountModule {}
