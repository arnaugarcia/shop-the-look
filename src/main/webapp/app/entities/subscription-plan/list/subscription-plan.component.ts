import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubscriptionPlan } from '../subscription-plan.model';
import { SubscriptionPlanService } from '../service/subscription-plan.service';
import { SubscriptionPlanDeleteDialogComponent } from '../delete/subscription-plan-delete-dialog.component';

@Component({
  selector: 'stl-subscription-plan',
  templateUrl: './subscription-plan.component.html',
})
export class SubscriptionPlanComponent implements OnInit {
  subscriptionPlans?: ISubscriptionPlan[];
  isLoading = false;

  constructor(protected subscriptionPlanService: SubscriptionPlanService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.subscriptionPlanService.query().subscribe(
      (res: HttpResponse<ISubscriptionPlan[]>) => {
        this.isLoading = false;
        this.subscriptionPlans = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: ISubscriptionPlan): number {
    return item.id!;
  }

  delete(subscriptionPlan: ISubscriptionPlan): void {
    const modalRef = this.modalService.open(SubscriptionPlanDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.subscriptionPlan = subscriptionPlan;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
