import { Component, OnInit } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';
import { SubscriptionService } from '../../service/subscription.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'],
})
export class SubscriptionComponent implements OnInit {
  public plans: ISubscriptionPlan[] = [];
  public error = false;

  constructor(private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {
    this.subscriptionService.query().subscribe(
      (response: HttpResponse<ISubscriptionPlan[]>) => {
        this.plans = response.body!;
      },
      (error: HttpErrorResponse) => {
        this.error = true;
        console.error('Error loading the subscription plans', error);
      }
    );
  }

  selectedPlan($event: ISubscriptionPlan): void {
    console.error($event);
  }
}
