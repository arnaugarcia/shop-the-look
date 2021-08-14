import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { NgModule } from '@angular/core';
import { SettingsComponent } from './pages/settings/settings.component';

const accountRoute: Routes = [
  {
    path: '',
    redirectTo: 'settings',
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'settings',
    component: SettingsComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(accountRoute)],
  exports: [RouterModule],
})
export class AccountRoutingModule {}
