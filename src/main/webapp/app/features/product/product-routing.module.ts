import { NgModule } from '@angular/core';
import { RouterModule, Routes } from '@angular/router';

import { UserRouteAccessService } from 'app/core/auth/user-route-access.service';
import { ProductComponent } from './pages/list/product.component';
import { ProductImportComponent } from './pages/import/product-import.component';
import { ProductCreateComponent } from './pages/create/product-create.component';

const productRoute: Routes = [
  {
    path: '',
    component: ProductComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'new',
    component: ProductCreateComponent,
    canActivate: [UserRouteAccessService],
  },
  {
    path: 'import',
    component: ProductImportComponent,
    canActivate: [UserRouteAccessService],
  },
];

@NgModule({
  imports: [RouterModule.forChild(productRoute)],
  exports: [RouterModule],
})
export class ProductRoutingModule {}
