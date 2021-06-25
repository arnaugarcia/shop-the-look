import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { GoogleFeedProductComponent } from '../list/google-feed-product.component';
import { GoogleFeedProductDetailComponent } from '../detail/google-feed-product-detail.component';
import { GoogleFeedProductUpdateComponent } from '../update/google-feed-product-update.component';
import { GoogleFeedProductRoutingResolveService } from './google-feed-product-routing-resolve.service';

const googleFeedProductRoute: Routes = [
  {
    path: '',
    component: GoogleFeedProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: GoogleFeedProductDetailComponent,
    resolve: {
      googleFeedProduct: GoogleFeedProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: GoogleFeedProductUpdateComponent,
    resolve: {
      googleFeedProduct: GoogleFeedProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/edit',
    component: GoogleFeedProductUpdateComponent,
    resolve: {
      googleFeedProduct: GoogleFeedProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(googleFeedProductRoute)],
  exports: [RouterModule],
})
export class GoogleFeedProductRoutingModule {}
