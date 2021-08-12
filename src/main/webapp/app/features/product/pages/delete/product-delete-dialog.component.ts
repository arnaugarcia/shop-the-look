import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ProductService } from '../../services/product.service';
import { IProduct } from '../../models/product.model';

@Component({
  templateUrl: './product-delete-dialog.component.html',
})
export class ProductDeleteDialogComponent {
  product?: IProduct;

  constructor(protected productService: ProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(reference: string): void {
    this.productService.delete(reference).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
