import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';
import { AccountService } from '../../../../core/auth/account.service';

@Component({
  selector: 'stl-pricing-plan',
  templateUrl: './pricing-plan.component.html',
  styleUrls: ['./pricing-plan.component.scss'],
})
export class PricingPlanComponent {
  @Input()
  public plan!: ISubscriptionPlan;

  @Output()
  public selectedPlan = new EventEmitter<ISubscriptionPlan>();

  public isAdmin = false;

  constructor(private accountService: AccountService) {
    this.isAdmin = this.accountService.isAdmin();
  }

  selectPlan(selectedPlan: ISubscriptionPlan): void {
    this.selectedPlan.emit(selectedPlan);
  }
}
