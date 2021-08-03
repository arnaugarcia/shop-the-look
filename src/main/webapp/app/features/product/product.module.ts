import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProductRoutingModule } from './product-routing.module';
import { ProductComponent } from './pages/list/product.component';
import { ProductDetailComponent } from './pages/detail/product-detail.component';
import { ProductDeleteDialogComponent } from './pages/delete/product-delete-dialog.component';
import { ProductCreateComponent } from './pages/create/product-create.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { ProductImportComponent } from './pages/import/product-import.component';
import { NgxFileDropModule } from 'ngx-file-drop';
import { DragAndDropDirective } from './directive/drag-and-drop.directive';

@NgModule({
  declarations: [
    ProductComponent,
    ProductCreateComponent,
    ProductImportComponent,
    ProductDetailComponent,
    ProductDeleteDialogComponent,
    DragAndDropDirective,
  ],
  imports: [SharedModule, ProductRoutingModule, NgxFileDropModule, ContentHeaderModule],
})
export class ProductModule {}
