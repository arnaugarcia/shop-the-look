import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize, map } from 'rxjs/operators';

import { Company, ICompany } from '../company.model';
import { CompanyService } from '../service/company.service';
import { IBillingAddress } from 'app/entities/billing-address/billing-address.model';
import { IUser } from 'app/entities/user/user.model';
import { UserService } from 'app/entities/user/user.service';
import { ISubscriptionPlan } from 'app/entities/subscription-plan/subscription-plan.model';
import { SubscriptionPlanService } from 'app/entities/subscription-plan/service/subscription-plan.service';
import { BillingAddressService } from '../../../features/account/service/billing-address.service';

@Component({
  selector: 'stl-company-update',
  templateUrl: './company-update.component.html',
})
export class CompanyUpdateComponent implements OnInit {
  isSaving = false;

  billingAddressesCollection: IBillingAddress[] = [];
  usersSharedCollection: IUser[] = [];
  subscriptionPlansSharedCollection: ISubscriptionPlan[] = [];

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    commercialName: [],
    nif: [null, [Validators.required]],
    logo: [],
    vat: [],
    url: [null, [Validators.required]],
    phone: [null, [Validators.required]],
    email: [null, [Validators.required]],
    type: [],
    token: [null, [Validators.required]],
    reference: [null, [Validators.required]],
    industry: [],
    companySize: [],
    billingAddress: [],
    users: [null, Validators.required],
    subscriptionPlan: [],
  });

  constructor(
    protected companyService: CompanyService,
    protected billingAddressService: BillingAddressService,
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

  trackBillingAddressById(index: number, item: IBillingAddress): number {
    return item.id!;
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
      commercialName: company.commercialName,
      nif: company.nif,
      logo: company.logo,
      vat: company.vat,
      url: company.url,
      phone: company.phone,
      email: company.email,
      type: company.type,
      token: company.token,
      reference: company.reference,
      industry: company.industry,
      companySize: company.companySize,
      billingAddress: company.billingAddress,
      users: company.users,
      subscriptionPlan: company.subscriptionPlan,
    });

    /* this.billingAddressesCollection = this.billingAddressService.addBillingAddressToCollectionIfMissing(
      this.billingAddressesCollection,
      company.billingAddress
    ); */
    this.usersSharedCollection = this.userService.addUserToCollectionIfMissing(this.usersSharedCollection, ...(company.users ?? []));
    this.subscriptionPlansSharedCollection = this.subscriptionPlanService.addSubscriptionPlanToCollectionIfMissing(
      this.subscriptionPlansSharedCollection,
      company.subscriptionPlan
    );
  }

  protected loadRelationshipsOptions(): void {
    /* this.billingAddressService
      .query({ filter: 'company-is-null' })
      .pipe(map((res: HttpResponse<IBillingAddress[]>) => res.body ?? []))
      .pipe(
        map((billingAddresses: IBillingAddress[]) =>
          this.billingAddressService.addBillingAddressToCollectionIfMissing(billingAddresses, this.editForm.get('billingAddress')!.value)
        )
      )
      .subscribe((billingAddresses: IBillingAddress[]) => (this.billingAddressesCollection = billingAddresses)); */

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
      commercialName: this.editForm.get(['commercialName'])!.value,
      nif: this.editForm.get(['nif'])!.value,
      logo: this.editForm.get(['logo'])!.value,
      vat: this.editForm.get(['vat'])!.value,
      url: this.editForm.get(['url'])!.value,
      phone: this.editForm.get(['phone'])!.value,
      email: this.editForm.get(['email'])!.value,
      type: this.editForm.get(['type'])!.value,
      token: this.editForm.get(['token'])!.value,
      reference: this.editForm.get(['reference'])!.value,
      industry: this.editForm.get(['industry'])!.value,
      companySize: this.editForm.get(['companySize'])!.value,
      billingAddress: this.editForm.get(['billingAddress'])!.value,
      users: this.editForm.get(['users'])!.value,
      subscriptionPlan: this.editForm.get(['subscriptionPlan'])!.value,
    };
  }
}
