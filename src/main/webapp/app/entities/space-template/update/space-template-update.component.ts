import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISpaceTemplate, SpaceTemplate } from '../space-template.model';
import { SpaceTemplateService } from '../service/space-template.service';

@Component({
  selector: 'stl-space-template-update',
  templateUrl: './space-template-update.component.html',
})
export class SpaceTemplateUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    maxProducts: [null, [Validators.required]],
    maxPhotos: [null, [Validators.required]],
    active: [],
  });

  constructor(protected spaceTemplateService: SpaceTemplateService, protected activatedRoute: ActivatedRoute, protected fb: FormBuilder) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ spaceTemplate }) => {
      this.updateForm(spaceTemplate);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const spaceTemplate = this.createFromForm();
    if (spaceTemplate.id !== undefined) {
      this.subscribeToSaveResponse(this.spaceTemplateService.update(spaceTemplate));
    } else {
      this.subscribeToSaveResponse(this.spaceTemplateService.create(spaceTemplate));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISpaceTemplate>>): void {
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

  protected updateForm(spaceTemplate: ISpaceTemplate): void {
    this.editForm.patchValue({
      id: spaceTemplate.id,
      name: spaceTemplate.name,
      description: spaceTemplate.description,
      maxProducts: spaceTemplate.maxProducts,
      maxPhotos: spaceTemplate.maxPhotos,
      active: spaceTemplate.active,
    });
  }

  protected createFromForm(): ISpaceTemplate {
    return {
      ...new SpaceTemplate(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      maxProducts: this.editForm.get(['maxProducts'])!.value,
      maxPhotos: this.editForm.get(['maxPhotos'])!.value,
      active: this.editForm.get(['active'])!.value,
    };
  }
}
