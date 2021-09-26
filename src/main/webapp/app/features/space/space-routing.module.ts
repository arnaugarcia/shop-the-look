import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { StudioComponent } from './pages/studio/studio.component';
import { CreateComponent } from './pages/create/create.component';
import { EnjoyComponent } from './pages/enjoy/enjoy.component';
import { CustomizeComponent } from './pages/customize/customize.component';
import { TemplateComponent } from './pages/template/template.component';
import { ListComponent } from './pages/list/list.component';
import { EditComponent } from './pages/edit/edit.component';
import { SpaceReferenceGuard } from './route/space-reference.guard';
import { SpaceRoutingResolveService } from './route/space-routing-resolve.service';
import { CompanyRoutingResolveService } from '../company/route/company-routing-resolve.service';

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
        path: ':space-reference',
        canActivate: [SpaceReferenceGuard],
        runGuardsAndResolvers: 'always',
        resolve: {
          space: SpaceRoutingResolveService,
        },
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
            path: 'enjoy',
            component: EnjoyComponent,
            resolve: {
              company: CompanyRoutingResolveService,
            },
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
