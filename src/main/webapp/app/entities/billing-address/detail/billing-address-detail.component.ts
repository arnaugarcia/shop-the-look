import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IBillingAddress } from '../billing-address.model';

@Component({
  selector: 'stl-billing-address-detail',
  templateUrl: './billing-address-detail.component.html',
})
export class BillingAddressDetailComponent implements OnInit {
  billingAddress: IBillingAddress | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billingAddress }) => {
      this.billingAddress = billingAddress;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
