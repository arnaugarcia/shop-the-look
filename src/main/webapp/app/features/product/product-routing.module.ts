import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductRoutingResolveService } from './product-routing-resolve.service';
import { ProductComponent } from './pages/list/product.component';
import { ProductDetailComponent } from './pages/detail/product-detail.component';
import { ProductCreateComponent } from './pages/create/product-create.component';

const productRoute: Routes = [
  {
    path: '',
    component: ProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: ':id/view',
    component: ProductDetailComponent,
    resolve: {
      product: ProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductCreateComponent,
    resolve: {
      product: ProductRoutingResolveService,
    },
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productRoute)],
  exports: [RouterModule],
})
export class ProductRoutingModule {}
