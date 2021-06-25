import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ICoordinate } from '../coordinate.model';
import { CoordinateService } from '../service/coordinate.service';

@Component({
  templateUrl: './coordinate-delete-dialog.component.html',
})
export class CoordinateDeleteDialogComponent {
  coordinate?: ICoordinate;

  constructor(protected coordinateService: CoordinateService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.coordinateService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
