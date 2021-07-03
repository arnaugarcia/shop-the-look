import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { IBillingAddress, BillingAddress } from '../billing-address.model';
import { BillingAddressService } from '../service/billing-address.service';

@Component({
  selector: 'stl-billing-address-update',
  templateUrl: './billing-address-update.component.html',
})
export class BillingAddressUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    address: [null, [Validators.required]],
    city: [null, [Validators.required]],
    province: [null, [Validators.required]],
    zipCode: [null, [Validators.required]],
    country: [null, [Validators.required]],
  });

  constructor(
    protected billingAddressService: BillingAddressService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ billingAddress }) => {
      this.updateForm(billingAddress);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const billingAddress = this.createFromForm();
    if (billingAddress.id !== undefined) {
      this.subscribeToSaveResponse(this.billingAddressService.update(billingAddress));
    } else {
      this.subscribeToSaveResponse(this.billingAddressService.create(billingAddress));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IBillingAddress>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(billingAddress: IBillingAddress): void {
    this.editForm.patchValue({
      id: billingAddress.id,
      address: billingAddress.address,
      city: billingAddress.city,
      province: billingAddress.province,
      zipCode: billingAddress.zipCode,
      country: billingAddress.country,
    });
  }

  protected createFromForm(): IBillingAddress {
    return {
      ...new BillingAddress(),
      id: this.editForm.get(['id'])!.value,
      address: this.editForm.get(['address'])!.value,
      city: this.editForm.get(['city'])!.value,
      province: this.editForm.get(['province'])!.value,
      zipCode: this.editForm.get(['zipCode'])!.value,
      country: this.editForm.get(['country'])!.value,
    };
  }
}
