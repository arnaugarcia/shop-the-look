import { Component, OnInit } from '@angular/core';
import { FormBuilder, FormGroup, Validators } from '@angular/forms';
import { CoreSidebarService } from '../../../../../@core/components/core-sidebar/core-sidebar.service';
import { User } from '../../../../admin/user-management/user-management.model';
import { LANGUAGES } from '../../../../config/language.constants';
import { UserManagementService } from '../../../../admin/user-management/service/user-management.service';
import { ActivatedRoute } from '@angular/router';
import { IEmployee } from '../../models/employee.model';

@Component({
  selector: 'stl-employee-add-sidebar',
  templateUrl: './employee-add-sidebar.component.html',
  styleUrls: ['./employee-add-sidebar.component.scss'],
})
export class EmployeeAddSidebarComponent implements OnInit {
  employee!: IEmployee;
  languages = LANGUAGES;
  authorities: string[] = [];
  isSaving = false;

  editForm = this.fb.group({
    id: [],
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
    activated: [],
    langKey: [],
    authorities: [],
  });

  constructor(
    private _coreSidebarService: CoreSidebarService,
    private userService: UserManagementService,
    private route: ActivatedRoute,
    private fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ user }) => {
      if (user) {
        this.employee = user;
        this.updateForm(user);
      }
    });
    this.userService.authorities().subscribe(authorities => (this.authorities = authorities));
  }

  previousState(): void {
    window.history.back();
  }

  toggleSidebar(name: string): void {
    this._coreSidebarService.getSidebarRegistry(name).toggleOpen();
  }

  submit(form: FormGroup): void {
    if (form.valid) {
      this.toggleSidebar('new-user-sidebar');
    }
  }

  save(): void {
    this.isSaving = true;
    this.updateUser(this.employee);
    if (this.employee.login === '') {
      this.userService.update(this.employee).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    } else {
      this.userService.create(this.employee).subscribe(
        () => this.onSaveSuccess(),
        () => this.onSaveError()
      );
    }
  }

  private updateForm(user: User): void {
    this.editForm.patchValue({
      id: user.id,
      login: user.login,
      firstName: user.firstName,
      lastName: user.lastName,
      email: user.email,
      activated: user.activated,
      langKey: user.langKey,
      authorities: user.authorities,
    });
  }

  private updateUser(user: User): void {
    user.login = this.editForm.get(['login'])!.value;
    user.firstName = this.editForm.get(['firstName'])!.value;
    user.lastName = this.editForm.get(['lastName'])!.value;
    user.email = this.editForm.get(['email'])!.value;
    user.activated = this.editForm.get(['activated'])!.value;
    user.langKey = this.editForm.get(['langKey'])!.value;
    user.authorities = this.editForm.get(['authorities'])!.value;
  }

  private onSaveSuccess(): void {
    this.isSaving = false;
    this.previousState();
  }

  private onSaveError(): void {
    this.isSaving = false;
  }
}
