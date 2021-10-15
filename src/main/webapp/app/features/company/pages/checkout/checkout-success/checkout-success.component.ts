import { Component, OnInit } from '@angular/core';
import { ISubscriptionPlan } from '../../../model/subscription-plan.model';
import { ActivatedRoute } from '@angular/router';

@Component({
  selector: 'stl-checkout-success',
  templateUrl: './checkout-success.component.html',
  styleUrls: ['./checkout-success.component.scss'],
})
export class CheckoutSuccessComponent implements OnInit {
  public subscription?: ISubscriptionPlan;
  private subscriptions?: ISubscriptionPlan[];

  constructor(private route: ActivatedRoute) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ subscriptions }) => {
      this.subscriptions = subscriptions;
      this.subscription = this.subscriptions?.find((subscription: ISubscriptionPlan) => subscription.current);
    });
  }
}
