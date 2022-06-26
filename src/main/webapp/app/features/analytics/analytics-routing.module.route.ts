import { RouterModule, Routes } from '@angular/router';
import { UserRouteAccessService } from '../../core/auth/user-route-access.service';
import { NgModule } from '@angular/core';
import { AnalyticsComponent } from './pages/analytics/analytics.component';

const analyticsRoute: Routes = [
  {
    path: '',
    component: AnalyticsComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(analyticsRoute)],
  exports: [RouterModule],
})
export class AnalyticsRoutingModule {}
