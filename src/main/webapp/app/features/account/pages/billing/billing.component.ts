import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { BillingAddress } from '../../model/billing-address.model';
import { BillingAddressService } from '../../service/billing-address.service';

@Component({
  selector: 'stl-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.scss'],
})
export class BillingComponent {
  public billingAddress = new BillingAddress();

  billingAddressForm = this.formBuilder.group({
    address: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(250)]],
    country: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    city: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    zipCode: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
    province: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
  });

  constructor(private formBuilder: FormBuilder, private billingAddressService: BillingAddressService) {}

  save(): void {
    console.error('saved!');
  }
}
