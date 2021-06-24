import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import * as dayjs from 'dayjs';
import { DATE_TIME_FORMAT } from 'app/config/input.constants';

import { IGoogleFeedProduct, GoogleFeedProduct } from '../google-feed-product.model';
import { GoogleFeedProductService } from '../service/google-feed-product.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';

@Component({
  selector: 'stl-google-feed-product-update',
  templateUrl: './google-feed-product-update.component.html',
})
export class GoogleFeedProductUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    sku: [null, [Validators.required]],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    link: [null, [Validators.required]],
    imageLink: [null, [Validators.required]],
    aditionalImageLink: [],
    mobileLink: [],
    availability: [null, [Validators.required]],
    availabilityDate: [],
    price: [null, [Validators.required]],
    salePrice: [null, [Validators.required]],
    brand: [],
    condition: [],
    adult: [],
    ageGroup: [],
    company: [],
  });

  constructor(
    protected googleFeedProductService: GoogleFeedProductService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ googleFeedProduct }) => {
      if (googleFeedProduct.id === undefined) {
        const today = dayjs().startOf('day');
        googleFeedProduct.availabilityDate = today;
      }

      this.updateForm(googleFeedProduct);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const googleFeedProduct = this.createFromForm();
    if (googleFeedProduct.id !== undefined) {
      this.subscribeToSaveResponse(this.googleFeedProductService.update(googleFeedProduct));
    } else {
      this.subscribeToSaveResponse(this.googleFeedProductService.create(googleFeedProduct));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IGoogleFeedProduct>>): void {
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

  protected updateForm(googleFeedProduct: IGoogleFeedProduct): void {
    this.editForm.patchValue({
      id: googleFeedProduct.id,
      sku: googleFeedProduct.sku,
      name: googleFeedProduct.name,
      description: googleFeedProduct.description,
      link: googleFeedProduct.link,
      imageLink: googleFeedProduct.imageLink,
      aditionalImageLink: googleFeedProduct.aditionalImageLink,
      mobileLink: googleFeedProduct.mobileLink,
      availability: googleFeedProduct.availability,
      availabilityDate: googleFeedProduct.availabilityDate ? googleFeedProduct.availabilityDate.format(DATE_TIME_FORMAT) : null,
      price: googleFeedProduct.price,
      salePrice: googleFeedProduct.salePrice,
      brand: googleFeedProduct.brand,
      condition: googleFeedProduct.condition,
      adult: googleFeedProduct.adult,
      ageGroup: googleFeedProduct.ageGroup,
      company: googleFeedProduct.company,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(
      this.companiesSharedCollection,
      googleFeedProduct.company
    );
  }

  protected loadRelationshipsOptions(): void {
    this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies));
  }

  protected createFromForm(): IGoogleFeedProduct {
    return {
      ...new GoogleFeedProduct(),
      id: this.editForm.get(['id'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      link: this.editForm.get(['link'])!.value,
      imageLink: this.editForm.get(['imageLink'])!.value,
      aditionalImageLink: this.editForm.get(['aditionalImageLink'])!.value,
      mobileLink: this.editForm.get(['mobileLink'])!.value,
      availability: this.editForm.get(['availability'])!.value,
      availabilityDate: this.editForm.get(['availabilityDate'])!.value
        ? dayjs(this.editForm.get(['availabilityDate'])!.value, DATE_TIME_FORMAT)
        : undefined,
      price: this.editForm.get(['price'])!.value,
      salePrice: this.editForm.get(['salePrice'])!.value,
      brand: this.editForm.get(['brand'])!.value,
      condition: this.editForm.get(['condition'])!.value,
      adult: this.editForm.get(['adult'])!.value,
      ageGroup: this.editForm.get(['ageGroup'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
