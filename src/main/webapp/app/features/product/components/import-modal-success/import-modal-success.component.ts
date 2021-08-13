import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'stl-import-modal-success',
  templateUrl: './import-modal-success.component.html',
  styleUrls: ['./import-modal-success.component.scss'],
})
export class ImportModalSuccessComponent {
  constructor(public activeModal: NgbActiveModal) {}
}
