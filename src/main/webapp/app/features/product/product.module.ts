import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProductRoutingModule } from './product-routing.module';
import { ProductComponent } from './pages/list/product.component';
import { ProductDetailComponent } from './pages/detail/product-detail.component';
import { ProductDeleteDialogComponent } from './pages/delete/product-delete-dialog.component';
import { ProductCreateComponent } from './pages/create/product-create.component';
import { FileUploadModule } from 'ng2-file-upload';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { NgxDatatableModule } from '@swimlane/ngx-datatable';
import { ProductImportComponent } from './pages/import/product-import.component';

@NgModule({
  declarations: [ProductComponent, ProductCreateComponent, ProductImportComponent, ProductDetailComponent, ProductDeleteDialogComponent],
  imports: [SharedModule, ProductRoutingModule, NgxDatatableModule, FileUploadModule, ContentHeaderModule],
})
export class ProductModule {}
