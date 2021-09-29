import { Component } from '@angular/core';
import { SpaceService } from '../../service/space.service';
import { ISpace } from '../../model/space.model';
import { HttpErrorResponse } from '@angular/common/http';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'stl-space-delete-dialog',
  templateUrl: './space-delete-dialog.component.html',
})
export class SpaceDeleteDialogComponent {
  public space?: ISpace;

  constructor(private spaceService: SpaceService, protected modal: NgbActiveModal) {}

  dismiss(): void {
    this.modal.dismiss();
  }

  confirmDelete(reference: string): void {
    this.spaceService.delete(reference).subscribe(
      () => {
        this.modal.close('deleted');
      },
      (error: HttpErrorResponse) => {
        this.modal.close(error.message);
      }
    );
  }
}
