import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'stl-company-modal-error',
  templateUrl: './company-modal-error.component.html',
})
export class CompanyModalErrorComponent {
  constructor(public activeModal: NgbActiveModal) {}
}
