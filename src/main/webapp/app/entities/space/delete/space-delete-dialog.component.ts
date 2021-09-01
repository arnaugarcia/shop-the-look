import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISpace } from '../space.model';
import { SpaceService } from '../service/space.service';

@Component({
  templateUrl: './space-delete-dialog.component.html',
})
export class SpaceDeleteDialogComponent {
  space?: ISpace;

  constructor(protected spaceService: SpaceService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(reference: string): void {
    this.spaceService.delete(reference).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
