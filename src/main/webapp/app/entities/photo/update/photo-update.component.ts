import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { IPhoto, Photo } from '../photo.model';
import { PhotoService } from '../service/photo.service';
import { ISpace } from 'app/entities/space/space.model';
import { SpaceService } from 'app/entities/space/service/space.service';
import { ISpaceTemplate } from 'app/entities/space-template/space-template.model';
import { SpaceTemplateService } from 'app/entities/space-template/service/space-template.service';

@Component({
  selector: 'stl-photo-update',
  templateUrl: './photo-update.component.html',
})
export class PhotoUpdateComponent implements OnInit {
  isSaving = false;

  spacesSharedCollection: ISpace[] = [];
  spaceTemplatesSharedCollection: ISpaceTemplate[] = [];

  editForm = this.fb.group({
    id: [],
    name: [],
    description: [],
    link: [null, [Validators.required]],
    order: [null, [Validators.required]],
    height: [null, [Validators.required]],
    width: [null, [Validators.required]],
    orientation: [],
    demo: [],
    space: [],
    spaceTemplate: [],
  });

  constructor(
    protected photoService: PhotoService,
    protected spaceService: SpaceService,
    protected spaceTemplateService: SpaceTemplateService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ photo }) => {
      this.updateForm(photo);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const photo = this.createFromForm();
    if (photo.id !== undefined) {
      this.subscribeToSaveResponse(this.photoService.update(photo));
    } else {
      this.subscribeToSaveResponse(this.photoService.create(photo));
    }
  }

  trackSpaceById(index: number, item: ISpace): number {
    return item.id!;
  }

  trackSpaceTemplateById(index: number, item: ISpaceTemplate): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<IPhoto>>): void {
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

  protected updateForm(photo: IPhoto): void {
    this.editForm.patchValue({
      id: photo.id,
      name: photo.name,
      description: photo.description,
      link: photo.link,
      order: photo.order,
      height: photo.height,
      width: photo.width,
      orientation: photo.orientation,
      demo: photo.demo,
      space: photo.space,
      spaceTemplate: photo.spaceTemplate,
    });

    this.spacesSharedCollection = this.spaceService.addSpaceToCollectionIfMissing(this.spacesSharedCollection, photo.space);
    this.spaceTemplatesSharedCollection = this.spaceTemplateService.addSpaceTemplateToCollectionIfMissing(
      this.spaceTemplatesSharedCollection,
      photo.spaceTemplate
    );
  }

  protected loadRelationshipsOptions(): void {
    this.spaceService
      .query()
      .pipe(map((res: HttpResponse<ISpace[]>) => res.body ?? []))
      .pipe(map((spaces: ISpace[]) => this.spaceService.addSpaceToCollectionIfMissing(spaces, this.editForm.get('space')!.value)))
      .subscribe((spaces: ISpace[]) => (this.spacesSharedCollection = spaces));

    this.spaceTemplateService
      .query()
      .pipe(map((res: HttpResponse<ISpaceTemplate[]>) => res.body ?? []))
      .pipe(
        map((spaceTemplates: ISpaceTemplate[]) =>
          this.spaceTemplateService.addSpaceTemplateToCollectionIfMissing(spaceTemplates, this.editForm.get('spaceTemplate')!.value)
        )
      )
      .subscribe((spaceTemplates: ISpaceTemplate[]) => (this.spaceTemplatesSharedCollection = spaceTemplates));
  }

  protected createFromForm(): IPhoto {
    return {
      ...new Photo(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      link: this.editForm.get(['link'])!.value,
      order: this.editForm.get(['order'])!.value,
      height: this.editForm.get(['height'])!.value,
      width: this.editForm.get(['width'])!.value,
      orientation: this.editForm.get(['orientation'])!.value,
      demo: this.editForm.get(['demo'])!.value,
      space: this.editForm.get(['space'])!.value,
      spaceTemplate: this.editForm.get(['spaceTemplate'])!.value,
    };
  }
}
