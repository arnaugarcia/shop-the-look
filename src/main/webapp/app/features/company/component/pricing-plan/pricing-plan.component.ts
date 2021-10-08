import { Component, Input } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';

@Component({
  selector: 'stl-pricing-plan',
  templateUrl: './pricing-plan.component.html',
  styleUrls: ['./pricing-plan.component.scss'],
})
export class PricingPlanComponent {
  @Input()
  plan!: ISubscriptionPlan;
}
