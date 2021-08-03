import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProductRoutingModule } from './product-routing.module';
import { ProductComponent } from './pages/list/product.component';
import { ProductUpdateComponent } from './pages/update/product-update.component';
import { ProductDetailComponent } from './pages/detail/product-detail.component';
import { ProductDeleteDialogComponent } from './pages/delete/product-delete-dialog.component';

@NgModule({
  declarations: [ProductComponent, ProductUpdateComponent, ProductDetailComponent, ProductDeleteDialogComponent],
  imports: [SharedModule, ProductRoutingModule],
})
export class ProductModule {}
