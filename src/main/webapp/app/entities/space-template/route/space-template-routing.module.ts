import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { SpaceTemplateComponent } from '../list/space-template.component';
import { SpaceTemplateDetailComponent } from '../detail/space-template-detail.component';
import { SpaceTemplateUpdateComponent } from '../update/space-template-update.component';
import { SpaceTemplateRoutingResolveService } from './space-template-routing-resolve.service';

const spaceTemplateRoute: Routes = [
  {
    path: '',
    component: SpaceTemplateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: SpaceTemplateDetailComponent,
    resolve: {
      spaceTemplate: SpaceTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: SpaceTemplateUpdateComponent,
    resolve: {
      spaceTemplate: SpaceTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: SpaceTemplateUpdateComponent,
    resolve: {
      spaceTemplate: SpaceTemplateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(spaceTemplateRoute)],
  exports: [RouterModule],
})
export class SpaceTemplateRoutingModule {}
