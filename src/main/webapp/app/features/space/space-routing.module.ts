import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudioComponent } from './pages/studio/studio.component';

const spaceRoute: Routes = [
  {
    path: '',
    component: StudioComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaceRoute)],
  exports: [RouterModule],
})
export class SpaceRoutingModule {}
