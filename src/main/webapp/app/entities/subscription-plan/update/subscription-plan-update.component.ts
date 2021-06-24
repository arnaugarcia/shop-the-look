import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { FormBuilder, Validators } from '@angular/forms';
import { ActivatedRoute } from '@angular/router';
import { Observable } from 'rxjs';
import { finalize } from 'rxjs/operators';

import { ISubscriptionPlan, SubscriptionPlan } from '../subscription-plan.model';
import { SubscriptionPlanService } from '../service/subscription-plan.service';

@Component({
  selector: 'stl-subscription-plan-update',
  templateUrl: './subscription-plan-update.component.html',
})
export class SubscriptionPlanUpdateComponent implements OnInit {
  isSaving = false;

  editForm = this.fb.group({
    id: [],
    name: [null, [Validators.required]],
    description: [],
    category: [null, [Validators.required]],
    maxProducts: [null, [Validators.required]],
    maxSpaces: [null, [Validators.required]],
    maxRequests: [null, [Validators.required]],
  });

  constructor(
    protected subscriptionPlanService: SubscriptionPlanService,
    protected activatedRoute: ActivatedRoute,
    protected fb: FormBuilder
  ) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ subscriptionPlan }) => {
      this.updateForm(subscriptionPlan);
    });
  }

  previousState(): void {
    window.history.back();
  }

  save(): void {
    this.isSaving = true;
    const subscriptionPlan = this.createFromForm();
    if (subscriptionPlan.id !== undefined) {
      this.subscribeToSaveResponse(this.subscriptionPlanService.update(subscriptionPlan));
    } else {
      this.subscribeToSaveResponse(this.subscriptionPlanService.create(subscriptionPlan));
    }
  }

  protected subscribeToSaveResponse(result: Observable<HttpResponse<ISubscriptionPlan>>): void {
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

  protected updateForm(subscriptionPlan: ISubscriptionPlan): void {
    this.editForm.patchValue({
      id: subscriptionPlan.id,
      name: subscriptionPlan.name,
      description: subscriptionPlan.description,
      category: subscriptionPlan.category,
      maxProducts: subscriptionPlan.maxProducts,
      maxSpaces: subscriptionPlan.maxSpaces,
      maxRequests: subscriptionPlan.maxRequests,
    });
  }

  protected createFromForm(): ISubscriptionPlan {
    return {
      ...new SubscriptionPlan(),
      id: this.editForm.get(['id'])!.value,
      name: this.editForm.get(['name'])!.value,
      description: this.editForm.get(['description'])!.value,
      category: this.editForm.get(['category'])!.value,
      maxProducts: this.editForm.get(['maxProducts'])!.value,
      maxSpaces: this.editForm.get(['maxSpaces'])!.value,
      maxRequests: this.editForm.get(['maxRequests'])!.value,
    };
  }
}
