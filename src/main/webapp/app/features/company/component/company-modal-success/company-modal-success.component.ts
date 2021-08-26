import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

@Component({
  selector: 'stl-company-modal-success',
  templateUrl: './company-modal-success.component.html',
})
export class CompanyModalSuccessComponent {
  constructor(public activeModal: NgbActiveModal) {}
}
