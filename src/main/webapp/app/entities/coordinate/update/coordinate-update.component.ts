import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICoordinate, Coordinate } from '../coordinate.model';
import { CoordinateService } from '../service/coordinate.service';
import { IPhoto } from 'app/entities/photo/photo.model';
import { PhotoService } from 'app/entities/photo/service/photo.service';

@Component({
  selector: 'stl-coordinate-update',
  templateUrl: './coordinate-update.component.html',
})
export class CoordinateUpdateComponent implements OnInit {
  isSaving = false;

  photosSharedCollection: IPhoto[] = [];

  editForm = this.fb.group({
    id: [],
    x: [],
    y: [],
    photo: [],
  });

  constructor(
    protected coordinateService: CoordinateService,
    protected photoService: PhotoService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ coordinate }) => {
      this.updateForm(coordinate);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const coordinate = this.createFromForm();
    if (coordinate.id !== undefined) {
      this.subscribeToSaveResponse(this.coordinateService.update(coordinate));
    } else {
      this.subscribeToSaveResponse(this.coordinateService.create(coordinate));
    }
  }

  trackPhotoById(index: number, item: IPhoto): number {
    return item.id!;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICoordinate>>): void {
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

  protected updateForm(coordinate: ICoordinate): void {
    this.editForm.patchValue({
      id: coordinate.id,
      x: coordinate.x,
      y: coordinate.y,
      photo: coordinate.photo,
    });

    this.photosSharedCollection = this.photoService.addPhotoToCollectionIfMissing(this.photosSharedCollection, coordinate.photo);
  }

  protected loadRelationshipsOptions(): void {
    this.photoService
      .query()
      .pipe(map((res: HttpResponse<IPhoto[]>) => res.body ?? []))
      .pipe(map((photos: IPhoto[]) => this.photoService.addPhotoToCollectionIfMissing(photos, this.editForm.get('photo')!.value)))
      .subscribe((photos: IPhoto[]) => (this.photosSharedCollection = photos));
  }

  protected createFromForm(): ICoordinate {
    return {
      ...new Coordinate(),
      id: this.editForm.get(['id'])!.value,
      x: this.editForm.get(['x'])!.value,
      y: this.editForm.get(['y'])!.value,
      photo: this.editForm.get(['photo'])!.value,
    };
  }
}
