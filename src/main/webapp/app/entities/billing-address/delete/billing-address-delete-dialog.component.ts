import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillingAddress } from '../billing-address.model';
import { BillingAddressService } from '../service/billing-address.service';

@Component({
  templateUrl: './billing-address-delete-dialog.component.html',
})
export class BillingAddressDeleteDialogComponent {
  billingAddress?: IBillingAddress;

  constructor(protected billingAddressService: BillingAddressService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.billingAddressService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
