import { Component, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ActivatedRoute, Router } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { ISpace, SpaceRequest } from '../../model/space.model';
import { StudioStore } from '../../store/studio.store';

@Component({
  selector: 'stl-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent implements OnInit {
  public spaceForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    description: ['', [Validators.maxLength(250)]],
  });

  private space?: ISpace;

  constructor(
    private formBuilder: FormBuilder,
    private spaceService: SpaceService,
    private route: ActivatedRoute,
    private router: Router,
    private studioStore: StudioStore
  ) {
    studioStore.navigate('create');
  }

  ngOnInit(): void {
    this.route.data.subscribe(({ space }) => {
      this.space = space;
      if (this.space) {
        this.updateForm(this.space);
      }
    });
  }

  onSubmit(): void {
    if (this.space) {
      this.spaceService.partialUpdate(this.createFromForm(), this.space.reference).subscribe(
        (response: HttpResponse<ISpace>) => this.onSuccess(response),
        (error: HttpErrorResponse) => this.onError(error)
      );
    } else {
      this.spaceService.create(this.createFromForm()).subscribe(
        (response: HttpResponse<ISpace>) => this.onSuccess(response),
        (error: HttpErrorResponse) => this.onError(error)
      );
    }
  }

  private onSuccess(response: HttpResponse<ISpace>): void {
    if (response.body) {
      this.router.navigate(['/spaces/studio', response.body.reference, 'template']);
    }
  }

  private updateForm(space: ISpace): void {
    this.spaceForm.patchValue({
      name: space.name,
      description: space.description,
    });
  }

  private createFromForm(): SpaceRequest {
    return {
      ...new SpaceRequest(this.spaceForm.get(['name'])!.value),
      description: this.spaceForm.get(['description'])!.value,
    };
  }

  private onError(error: HttpErrorResponse): void {
    console.error('Error creating/updating the space', error);
  }
}
