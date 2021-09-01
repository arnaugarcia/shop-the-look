import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudioComponent } from './pages/studio/studio.component';
import { CreateComponent } from './pages/create/create.component';
import { PublishComponent } from './pages/publish/publish.component';
import { EnjoyComponent } from './pages/enjoy/enjoy.component';
import { CustomizeComponent } from './pages/customize/customize.component';
import { TemplateComponent } from './pages/template/template.component';
import { ListComponent } from './pages/list/list.component';
import { EditComponent } from './pages/edit/edit.component';

const spaceRoute: Routes = [
  {
    path: '',
    component: ListComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'studio',
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
        path: ':reference/edit',
        component: EditComponent,
      },
      {
        path: ':reference/template',
        component: TemplateComponent,
      },
      {
        path: ':reference/customize',
        component: CustomizeComponent,
      },
      {
        path: ':reference/publish',
        component: PublishComponent,
      },
      {
        path: ':reference/enjoy',
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
