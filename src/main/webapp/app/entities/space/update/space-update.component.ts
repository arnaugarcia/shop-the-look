import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISpace, Space } from '../space.model';
import { SpaceService } from '../service/space.service';
import { CompanyService } from '../../../features/company/service/company.service';
import { ICompany } from '../../../features/company/model/company.model';

@Component({
  selector: 'stl-space-update',
  templateUrl: './space-update.component.html',
})
export class SpaceUpdateComponent implements OnInit {
  isSaving = false;

  companiesSharedCollection: ICompany[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    active: [],
    reference: [null, [Validators.required]],
    description: [],
    maxPhotos: [],
    visible: [],
    company: [null, Validators.required],
  });

  constructor(
    protected spaceService: SpaceService,
    protected companyService: CompanyService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ space }) => {
      this.updateForm(space);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const space = this.createFromForm();
    if (space.id !== undefined) {
      this.subscribeToSaveResponse(this.spaceService.update(space));
    } else {
      this.subscribeToSaveResponse(this.spaceService.create(space));
    }
  }

  trackCompanyById(index: number, item: ICompany): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpace>>): void {
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

  protected updateForm(space: ISpace): void {
    this.editForm.patchValue({
      id: space.id,
      name: space.name,
      active: space.active,
      reference: space.reference,
      description: space.description,
      maxPhotos: space.maxPhotos,
      visible: space.visible,
      company: space.company,
    });

    // this.companiesSharedCollection = this.companyService.addCompanyToCollectionIfMissing(this.companiesSharedCollection, space.company);
  }

  protected loadRelationshipsOptions(): void {
    /* this.companyService
      .query()
      .pipe(map((res: HttpResponse<ICompany[]>) => res.body ?? []))
      .pipe(
        map((companies: ICompany[]) => this.companyService.addCompanyToCollectionIfMissing(companies, this.editForm.get('company')!.value))
      )
      .subscribe((companies: ICompany[]) => (this.companiesSharedCollection = companies)); */
  }

  protected createFromForm(): ISpace {
    return {
      ...new Space(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      active: this.editForm.get(['active'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      description: this.editForm.get(['description'])!.value,
      maxPhotos: this.editForm.get(['maxPhotos'])!.value,
      visible: this.editForm.get(['visible'])!.value,
      company: this.editForm.get(['company'])!.value,
    };
  }
}
