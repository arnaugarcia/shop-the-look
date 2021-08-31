import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudioComponent } from './pages/studio/studio.component';
import { CreateComponent } from './pages/create/create.component';
import { PublishComponent } from './pages/publish/publish.component';
import { EnjoyComponent } from './pages/enjoy/enjoy.component';
import { CustomizeComponent } from './pages/customize/customize.component';

const spaceRoute: Routes = [
  {
    path: '',
    component: StudioComponent,
    canActivate: [UserRouteAccessService],
    children: [
      {
        path: '',
        redirectTo: 'create',
      },
      {
        path: 'create',
        component: CreateComponent,
      },
      {
        path: 'customize',
        component: CustomizeComponent,
      },
      {
        path: 'publish',
        component: PublishComponent,
      },
      {
        path: 'enjoy',
        component: EnjoyComponent,
      },
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaceRoute)],
  exports: [RouterModule],
})
export class SpaceRoutingModule {}
