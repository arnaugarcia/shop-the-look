import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { ICompany } from '../../model/company.model';
import { CompanyService } from '../../service/company.service';

@Component({
  templateUrl: './company-modal-delete.component.html',
})
export class CompanyModalDeleteComponent {
  company?: ICompany;

  constructor(protected companyService: CompanyService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(reference: string): void {
    this.companyService.delete(reference).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
