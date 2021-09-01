import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { SpaceService } from '../../../../entities/space/service/space.service';
import { ISpace, SpaceRequest } from '../../../../entities/space/space.model';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';

@Component({
  selector: 'stl-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent {
  public spaceForm = this.formBuilder.group({
    title: ['', [Validators.required, Validators.maxLength(50)]],
    description: ['', [Validators.maxLength(250)]],
  });

  constructor(private formBuilder: FormBuilder, private spaceService: SpaceService, private router: Router) {}

  onSubmit(): void {
    this.spaceService.create(this.createFromForm()).subscribe((response: HttpResponse<ISpace>) => {
      if (response.body) {
        this.router.navigate([response.body.reference, '/template']);
      }
    });
  }

  private createFromForm(): SpaceRequest {
    return {
      ...new SpaceRequest(),
      name: this.spaceForm.get(['name'])!.value,
      description: this.spaceForm.get(['description'])!.value,
    };
  }
}
