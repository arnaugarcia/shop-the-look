import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoogleFeedProduct } from '../google-feed-product.model';
import { GoogleFeedProductService } from '../service/google-feed-product.service';

@Component({
  templateUrl: './google-feed-product-delete-dialog.component.html',
})
export class GoogleFeedProductDeleteDialogComponent {
  googleFeedProduct?: IGoogleFeedProduct;

  constructor(protected googleFeedProductService: GoogleFeedProductService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.googleFeedProductService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
