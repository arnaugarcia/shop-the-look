import { Component } from '@angular/core';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';

import { ISubscriptionPlan } from '../subscription-plan.model';
import { SubscriptionPlanService } from '../service/subscription-plan.service';

@Component({
  templateUrl: './subscription-plan-delete-dialog.component.html',
})
export class SubscriptionPlanDeleteDialogComponent {
  subscriptionPlan?: ISubscriptionPlan;

  constructor(protected subscriptionPlanService: SubscriptionPlanService, protected activeModal: NgbActiveModal) {}

  cancel(): void {
    this.activeModal.dismiss();
  }

  confirmDelete(id: number): void {
    this.subscriptionPlanService.delete(id).subscribe(() => {
      this.activeModal.close('deleted');
    });
  }
}
