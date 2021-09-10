import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { ISpace, SpaceRequest } from '../../model/space.model';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'stl-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent {
  public spaceForm = this.formBuilder.group({
    name: ['', [Validators.required, Validators.maxLength(50)]],
    description: ['', [Validators.maxLength(250)]],
  });

  constructor(
    private formBuilder: FormBuilder,
    private spaceService: SpaceService,
    private router: Router,
    private studioService: StudioService
  ) {
    studioService.navigate('create');
  }

  onSubmit(): void {
    this.spaceService.create(this.createFromForm()).subscribe((response: HttpResponse<ISpace>) => {
      if (response.body) {
        this.router.navigate(['/spaces/studio', response.body.reference, 'template']);
      }
    });
  }

  private createFromForm(): SpaceRequest {
    return {
      ...new SpaceRequest(this.spaceForm.get(['name'])!.value),
      description: this.spaceForm.get(['description'])!.value,
    };
  }
}
