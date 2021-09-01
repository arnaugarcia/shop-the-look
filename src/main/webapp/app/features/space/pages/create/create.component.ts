import { Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { SpaceService } from '../../../../entities/space/service/space.service';
import { SpaceRequest } from '../../../../entities/space/space.model';

@Component({
  selector: 'stl-create',
  templateUrl: './create.component.html',
  styleUrls: ['./create.component.scss'],
})
export class CreateComponent {
  public spaceForm = this.formBuilder.group([
    {
      title: ['', [Validators.required, Validators.maxLength(50)]],
      description: ['', [Validators.maxLength(250)]],
    },
  ]);

  constructor(private formBuilder: FormBuilder, private spaceService: SpaceService) {}

  onSubmit(): void {
    this.spaceService.create(new SpaceRequest('Test', 'test'));
  }
}
