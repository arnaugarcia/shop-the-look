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
import { SpaceReferenceGuard } from './route/space-reference.guard';

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
        path: 'create',
        component: CreateComponent,
      },
      {
        path: ':reference',
        canActivate: [SpaceReferenceGuard],
        children: [
          {
            path: 'edit',
            component: EditComponent,
          },
          {
            path: 'template',
            component: TemplateComponent,
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
    ],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaceRoute)],
  exports: [RouterModule],
})
export class SpaceRoutingModule {}
