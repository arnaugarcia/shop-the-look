import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'stl-import-modal-error',
  templateUrl: './import-modal-error.component.html',
  styleUrls: ['./import-modal-error.component.scss'],
})
export class ImportModalErrorComponent {
  constructor(public activeModal: NgbActiveModal) {}
}
