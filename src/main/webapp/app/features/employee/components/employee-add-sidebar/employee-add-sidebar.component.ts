import { AfterViewInit, Component, OnDestroy, OnInit } from '@angular/core';
import { FormBuilder, Validators } from '@angular/forms';
import { CoreSidebarService } from '../../../../../@core/components/core-sidebar/core-sidebar.service';
import { Router } from '@angular/router';
import { EmployeeRequest } from '../../models/employee.model';
import { EmployeeService } from '../../services/employee.service';
import { CoreSidebarComponent } from '../../../../../@core/components/core-sidebar/core-sidebar.component';
import { ICompany } from '../../../../entities/company/company.model';
import { CompanyService } from '../../../../entities/company/service/company.service';
import { HttpResponse } from '@angular/common/http';
import { AccountService } from '../../../../core/auth/account.service';
import { Subscription } from 'rxjs';

@Component({
  selector: 'stl-employee-add-sidebar',
  templateUrl: './employee-add-sidebar.component.html',
  styleUrls: ['./employee-add-sidebar.component.scss'],
})
export class EmployeeAddSidebarComponent implements OnInit, AfterViewInit, OnDestroy {
  readonly NEW_USER_SIDEBAR_KEY = 'new-user-sidebar';
  employeeRequest = new EmployeeRequest();
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
    company: ['', []],
  });

  public sidebar!: CoreSidebarComponent;
  public companies: ICompany[] = [];
  private subscriptions = new Subscription();

  constructor(
    private _coreSidebarService: CoreSidebarService,
    private employeeService: EmployeeService,
    private companyService: CompanyService,
    private accountService: AccountService,
    private router: Router,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    if (this.accountService.isAdmin()) {
      this.subscriptions.add(
        this.companyService.query().subscribe((response: HttpResponse<ICompany[]>) => {
          this.companies = response.body!;
        })
      );
    }
  }

  ngAfterViewInit(): void {
    this.sidebar = this._coreSidebarService.getSidebarRegistry(this.NEW_USER_SIDEBAR_KEY);
  }

  ngOnDestroy(): void {
    this.subscriptions.unsubscribe();
  }

  save(): void {
    this.isSaving = true;
    this.updateEmployee(this.employeeRequest);
    this.subscriptions.add(
      this.employeeService.create(this.employeeRequest).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      )
    );
  }

  private updateEmployee(request: EmployeeRequest): void {
    request.login = this.newEmployeeForm.get(['login'])!.value;
    request.firstName = this.newEmployeeForm.get(['firstName'])!.value;
    request.lastName = this.newEmployeeForm.get(['lastName'])!.value;
    request.email = this.newEmployeeForm.get(['email'])!.value;
    request.companyReference = this.newEmployeeForm.get(['company'])!.value;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.sidebar.event('success');
    this.sidebar.close();
  }

  private onSaveError(): void {
    this.isSaving = false;
    this.sidebar.event('error');
  }
}
