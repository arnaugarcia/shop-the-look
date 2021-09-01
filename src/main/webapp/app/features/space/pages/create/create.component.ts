import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { HttpResponse } from '@angular/common/http';
import { Router } from '@angular/router';
import { SpaceService } from '../../service/space.service';
import { ISpace, SpaceRequest } from '../../model/space.model';

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
