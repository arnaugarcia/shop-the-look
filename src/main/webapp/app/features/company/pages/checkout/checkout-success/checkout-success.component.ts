import { Component } from '@angular/core';
import { ISubscriptionPlan } from '../../../model/subscription-plan.model';

@Component({
  selector: 'stl-checkout-success',
  templateUrl: './checkout-success.component.html',
  styleUrls: ['./checkout-success.component.scss'],
})
export class CheckoutSuccessComponent {
  subscription?: ISubscriptionPlan;
}
