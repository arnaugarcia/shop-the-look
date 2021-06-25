import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpaceTemplate } from '../space-template.model';
import { SpaceTemplateService } from '../service/space-template.service';

@Component({
  templateUrl: './space-template-delete-dialog.component.html',
})
export class SpaceTemplateDeleteDialogComponent {
  spaceTemplate?: ISpaceTemplate;

  constructor(protected spaceTemplateService: SpaceTemplateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.spaceTemplateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
