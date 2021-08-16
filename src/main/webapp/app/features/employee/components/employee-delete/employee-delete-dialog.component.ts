import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { EmployeeService } from '../../services/employee.service';
import { IEmployee } from '../../models/employee.model';

@Component({
  templateUrl: './employee-delete-dialog.component.html',
})
export class EmployeeDeleteDialogComponent {
  employee?: IEmployee;

  constructor(protected employeeService: EmployeeService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(login: string): void {
    this.employeeService.remove(login).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
