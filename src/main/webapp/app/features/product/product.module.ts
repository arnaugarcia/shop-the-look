import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ProductRoutingModule } from './product-routing.module';
import { ProductComponent } from './pages/list/product.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { ProductImportComponent } from './pages/import/product-import.component';
import { DragAndDropDirective } from './directive/drag-and-drop.directive';
import { FileUploadModule } from 'ng2-file-upload';
import { NgxPaginationModule } from 'ngx-pagination';
import { ImportModalSuccessComponent } from './components/import-modal-success/import-modal-success.component';
import { ImportModalErrorComponent } from './components/import-modal-error/import-modal-error.component';
import { ProductImportListItemComponent } from './pages/import/produt-import-list-item/product-import-list-item.component';

@NgModule({
  declarations: [
    ProductComponent,
    ProductImportComponent,
    DragAndDropDirective,
    ImportModalSuccessComponent,
    ImportModalErrorComponent,
    ProductImportListItemComponent,
  ],
  imports: [SharedModule, ProductRoutingModule, NgxPaginationModule, ContentHeaderModule, FileUploadModule],
})
export class ProductModule {}
