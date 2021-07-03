import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IBillingAddress } from '../billing-address.model';
import { BillingAddressService } from '../service/billing-address.service';
import { BillingAddressDeleteDialogComponent } from '../delete/billing-address-delete-dialog.component';

@Component({
  selector: 'stl-billing-address',
  templateUrl: './billing-address.component.html',
})
export class BillingAddressComponent implements OnInit {
  billingAddresses?: IBillingAddress[];
  isLoading = false;

  constructor(protected billingAddressService: BillingAddressService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.billingAddressService.query().subscribe(
      (res: HttpResponse<IBillingAddress[]>) => {
        this.isLoading = false;
        this.billingAddresses = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IBillingAddress): number {
    return item.id!;
  }

  delete(billingAddress: IBillingAddress): void {
    const modalRef = this.modalService.open(BillingAddressDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.billingAddress = billingAddress;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
