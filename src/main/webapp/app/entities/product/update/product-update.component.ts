import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IProduct, Product } from '../product.model';
import { ProductService } from '../service/product.service';
import { ICompany } from 'app/entities/company/company.model';
import { CompanyService } from 'app/entities/company/service/company.service';
import { ICoordinate } from 'app/entities/coordinate/coordinate.model';
import { CoordinateService } from 'app/entities/coordinate/service/coordinate.service';

@Component({
  selector: 'stl-product-update',
  templateUrl: './product-update.component.html',
})
export class ProductUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];
  coordinatesSharedCollection: ICoordinate[] = [];

  editForm = this.fb.group({
    id: [],
    sku: [null, [Validators.required]],
    name: [null, [Validators.required]],
    description: [null, [Validators.required]],
    link: [null, [Validators.required]],
    imageLink: [null, [Validators.required]],
    aditionalImageLink: [],
    availability: [null, [Validators.required]],
    price: [null, [Validators.required]],
    category: [],
    company: [null, Validators.required],
    coordinate: [],
  });

  constructor(
    protected productService: ProductService,
    protected companyService: CompanyService,
    protected coordinateService: CoordinateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ product }) => {
      this.updateForm(product);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const product = this.createFromForm();
    if (product.id !== undefined) {
      this.subscribeToSaveResponse(this.productService.update(product));
    } else {
      this.subscribeToSaveResponse(this.productService.create(product));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  trackCoordinateById(index: number, item: ICoordinate): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IProduct>>): void {
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

  protected updateForm(product: IProduct): void {
    this.editForm.patchValue({
      id: product.id,
      sku: product.sku,
      name: product.name,
      description: product.description,
      link: product.link,
      imageLink: product.imageLink,
      aditionalImageLink: product.aditionalImageLink,
      availability: product.availability,
      price: product.price,
      category: product.category,
      company: product.company,
      coordinate: product.coordinate,
    });

    this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, product.company);
    this.coordinatesSharedCollection = this.coordinateService.addCoordinateToCollectionIfMissing(
      this.coordinatesSharedCollection,
      product.coordinate
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

    this.coordinateService
      .query()
      .pipe(map((res: HttpResponse<ICoordinate[]>) => res.body ?? []))
      .pipe(
        map((coordinates: ICoordinate[]) =>
          this.coordinateService.addCoordinateToCollectionIfMissing(coordinates, this.editForm.get('coordinate')!.value)
        )
      )
      .subscribe((coordinates: ICoordinate[]) => (this.coordinatesSharedCollection = coordinates));
  }

  protected createFromForm(): IProduct {
    return {
      ...new Product(),
      id: this.editForm.get(['id'])!.value,
      sku: this.editForm.get(['sku'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      link: this.editForm.get(['link'])!.value,
      imageLink: this.editForm.get(['imageLink'])!.value,
      aditionalImageLink: this.editForm.get(['aditionalImageLink'])!.value,
      availability: this.editForm.get(['availability'])!.value,
      price: this.editForm.get(['price'])!.value,
      category: this.editForm.get(['category'])!.value,
      company: this.editForm.get(['company'])!.value,
      coordinate: this.editForm.get(['coordinate'])!.value,
    };
  }
}
