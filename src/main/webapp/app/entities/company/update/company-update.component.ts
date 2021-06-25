import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { ICompany, Company } from '../company.model';
import { CompanyService } from '../service/company.service';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISubscriptionPlan } from 'app/entities/subscription-plan/subscription-plan.model';
import { SubscriptionPlanService } from 'app/entities/subscription-plan/service/subscription-plan.service';

@Component({
  selector: 'stl-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  usersSharedCollection: IUser[] = [];
  subscriptionPlansSharedCollection: ISubscriptionPlan[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    cif: [null, [Validators.required]],
    token: [],
    industry: [],
    companySize: [],
    users: [null, Validators.required],
    subscriptionPlan: [null, Validators.required],
  });

  constructor(
    protected companyService: CompanyService,
    protected userService: UserService,
    protected subscriptionPlanService: SubscriptionPlanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ company }) => {
      this.updateForm(company);

      this.loadRelationshipsOptions();
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const company = this.createFromForm();
    if (company.id !== undefined) {
      this.subscribeToSaveResponse(this.companyService.update(company));
    } else {
      this.subscribeToSaveResponse(this.companyService.create(company));
    }
  }

  trackUserById(index: number, item: IUser): number {
    return item.id!;
  }

  trackSubscriptionPlanById(index: number, item: ISubscriptionPlan): number {
    return item.id!;
  }

  getSelectedUser(option: IUser, selectedVals?: IUser[]): IUser {
    if (selectedVals) {
      for (const selectedVal of selectedVals) {
        if (option.id === selectedVal.id) {
          return selectedVal;
        }
      }
    }
    return option;
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ICompany>>): void {
    result.pipe(finalize(() => this.onSaveFinalize())).subscribe(
      () => this.onSaveSuccess(),
      () => this.onSaveError()
    );
  }

  protected onSaveSuccess(): void {
    this.previousState();
  }

  protected onSaveError(): void {
    // Api for inheritance.
  }

  protected onSaveFinalize(): void {
    this.isSaving = false;
  }

  protected updateForm(company: ICompany): void {
    this.editForm.patchValue({
      id: company.id,
      name: company.name,
      cif: company.cif,
      token: company.token,
      industry: company.industry,
      companySize: company.companySize,
      users: company.users,
      subscriptionPlan: company.subscriptionPlan,
    });

    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ...(company.users ?? []));
    this.subscriptionPlansSharedCollection = this.subscriptionPlanService.addSubscriptionPlanToCollectionIfMissing(
      this.subscriptionPlansSharedCollection,
      company.subscriptionPlan
    );
  }

  protected loadRelationshipsOptions(): void {
    this.userService
      .query()
      .pipe(map((res: HttpResponse<IUser[]>) => res.body ?? []))
      .pipe(map((users: IUser[]) => this.userService.addUserToCollectionIfMissing(users, ...(this.editForm.get('users')!.value ?? []))))
      .subscribe((users: IUser[]) => (this.usersSharedCollection = users));

    this.subscriptionPlanService
      .query()
      .pipe(map((res: HttpResponse<ISubscriptionPlan[]>) => res.body ?? []))
      .pipe(
        map((subscriptionPlans: ISubscriptionPlan[]) =>
          this.subscriptionPlanService.addSubscriptionPlanToCollectionIfMissing(
            subscriptionPlans,
            this.editForm.get('subscriptionPlan')!.value
          )
        )
      )
      .subscribe((subscriptionPlans: ISubscriptionPlan[]) => (this.subscriptionPlansSharedCollection = subscriptionPlans));
  }

  protected createFromForm(): ICompany {
    return {
      ...new Company(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      cif: this.editForm.get(['cif'])!.value,
      token: this.editForm.get(['token'])!.value,
      industry: this.editForm.get(['industry'])!.value,
      companySize: this.editForm.get(['companySize'])!.value,
      users: this.editForm.get(['users'])!.value,
      subscriptionPlan: this.editForm.get(['subscriptionPlan'])!.value,
    };
  }
}
