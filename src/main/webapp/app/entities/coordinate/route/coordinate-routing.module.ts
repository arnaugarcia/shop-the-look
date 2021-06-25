import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { CoordinateComponent } from '../list/coordinate.component';
import { CoordinateDetailComponent } from '../detail/coordinate-detail.component';
import { CoordinateUpdateComponent } from '../update/coordinate-update.component';
import { CoordinateRoutingResolveService } from './coordinate-routing-resolve.service';

const coordinateRoute: Routes = [
  {
    path: '',
    component: CoordinateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: CoordinateDetailComponent,
    resolve: {
      coordinate: CoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: CoordinateUpdateComponent,
    resolve: {
      coordinate: CoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: CoordinateUpdateComponent,
    resolve: {
      coordinate: CoordinateRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(coordinateRoute)],
  exports: [RouterModule],
})
export class CoordinateRoutingModule {}
