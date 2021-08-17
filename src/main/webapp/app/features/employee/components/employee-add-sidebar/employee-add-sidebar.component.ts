import { AfterViewInit, Component } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CoreSidebarService } from '../../../../../@core/components/core-sidebar/core-sidebar.service';
import { Router } from '@angular/router';
import { Employee, IEmployee } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { CoreSidebarComponent } from '../../../../../@core/components/core-sidebar/core-sidebar.component';

@Component({
  selector: 'stl-employee-add-sidebar',
  templateUrl: './employee-add-sidebar.component.html',
  styleUrls: ['./employee-add-sidebar.component.scss'],
})
export class EmployeeAddSidebarComponent implements AfterViewInit {
  readonly NEW_USER_SIDEBAR_KEY = 'new-user-sidebar';
  employee: IEmployee = new Employee();
  isSaving = false;

  newEmployeeForm = this.fb.group({
    login: [
      '',
      [
        Validators.required,
        Validators.minLength(1),
        Validators.maxLength(50),
        Validators.pattern('^[a-zA-Z0-9!$&*+=?^_`{|}~.-]+@[a-zA-Z0-9-]+(?:\\.[a-zA-Z0-9-]+)*$|^[_.@A-Za-z0-9-]+$'),
      ],
    ],
    firstName: ['', [Validators.maxLength(50)]],
    lastName: ['', [Validators.maxLength(50)]],
    email: ['', [Validators.minLength(5), Validators.maxLength(254), Validators.email]],
  });

  public sidebar!: CoreSidebarComponent;

  constructor(
    private _coreSidebarService: CoreSidebarService,
    private employeeService: EmployeeService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngAfterViewInit(): void {
    this.sidebar = this._coreSidebarService.getSidebarRegistry(this.NEW_USER_SIDEBAR_KEY);
  }

  save(): void {
    this.isSaving = true;
    this.updateEmployee(this.employee);
    this.employeeService.create(this.employee).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  private updateEmployee(employee: IEmployee): void {
    employee.login = this.newEmployeeForm.get(['login'])!.value;
    employee.firstName = this.newEmployeeForm.get(['firstName'])!.value;
    employee.lastName = this.newEmployeeForm.get(['lastName'])!.value;
    employee.email = this.newEmployeeForm.get(['email'])!.value;
  }

  private onSaveSuccess(): void {
    this.router.navigate(['/employees']);
    this.isSaving = false;
    this.sidebar.event('success');
  }

  private onSaveError(): void {
    this.isSaving = false;
    this.sidebar.event('error');
  }
}
