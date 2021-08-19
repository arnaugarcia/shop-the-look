import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { BillingAddress, IBillingAddress } from '../../model/billing-address.model';
import { BillingAddressService } from '../../service/billing-address.service';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { HttpResponse } from '@angular/common/http';
import { finalize } from 'rxjs/operators';

@Component({
  selector: 'stl-billing',
  templateUrl: './billing.component.html',
  styleUrls: ['./billing.component.scss'],
})
export class BillingComponent implements OnInit {
  public billingAddress = new BillingAddress();

  billingAddressForm = this.formBuilder.group({
    address: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(250)]],
    country: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    city: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
    zipCode: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(15)]],
    province: ['', [Validators.required, Validators.minLength(5), Validators.maxLength(50)]],
  });
  private isSaving = false;

  constructor(
    private formBuilder: FormBuilder,
    private billingAddressService: BillingAddressService,
    private activatedRoute: ActivatedRoute
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billingAddress }) => {
      this.updateForm(billingAddress);
    });
  }

  save(): void {
    this.isSaving = true;
    const billingAddress = this.createFromForm();
    this.subscribeToSaveResponse(this.billingAddressService.update(billingAddress));
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillingAddress>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(null, () => this.onSaveError());
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected createFromForm(): IBillingAddress {
    return {
      ...new BillingAddress(),
      address: this.billingAddressForm.get(['address'])!.value,
      city: this.billingAddressForm.get(['city'])!.value,
      country: this.billingAddressForm.get(['country'])!.value,
      zipCode: this.billingAddressForm.get(['zipCode'])!.value,
      province: this.billingAddressForm.get(['province'])!.value,
    };
  }

  private updateForm(billingAddress: IBillingAddress): void {
    this.billingAddressForm.patchValue({
      address: billingAddress.address,
      city: billingAddress.city,
      country: billingAddress.country,
      zipCode: billingAddress.zipCode,
      province: billingAddress.province,
    });
  }
}
