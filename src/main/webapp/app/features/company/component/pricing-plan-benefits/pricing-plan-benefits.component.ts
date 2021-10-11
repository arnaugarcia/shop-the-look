import { Component, Input } from '@angular/core';
import { ISubscriptionBenefits } from '../../model/subscription-plan.model';

@Component({
  selector: 'stl-pricing-plan-benefits',
  templateUrl: './pricing-plan-benefits.component.html',
  styleUrls: ['./pricing-plan-benefits.component.scss'],
})
export class PricingPlanBenefitsComponent {
  @Input()
  public customPlan!: boolean;

  @Input()
  public benefits!: ISubscriptionBenefits;
}
