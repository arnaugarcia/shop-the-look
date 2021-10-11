import { Component, OnInit } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';
import { ActivatedRoute } from '@angular/router';
import { SubscriptionService } from '../../service/subscription.service';
import { AccountService } from '../../../../core/auth/account.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'],
})
export class SubscriptionComponent implements OnInit {
  public subscriptions: ISubscriptionPlan[] = [];
  private companyReference?: string;

  constructor(private route: ActivatedRoute, private accountService: AccountService, private subscriptionService: SubscriptionService) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ subscriptions }) => {
      this.subscriptions = subscriptions;
    });
    this.companyReference = this.route.parent?.snapshot.params.reference;
  }

  selectedPlan(selectedPlan: ISubscriptionPlan): void {
    if (selectedPlan.custom) {
      window.open('mailto:contact@weareklai.com?subject=[Shop The Look] - Custom pricing', '_blank');
    }
    if (this.accountService.isAdmin()) {
      this.subscriptionService.updateForCompany(this.companyReference!, selectedPlan.reference).subscribe(this.loadAll, this.onError);
    }
  }
  private loadAll(): void {
    if (this.companyReference) {
      this.subscriptionService.queryForCompany(this.companyReference).subscribe(this.onSuccess, this.onError);
    } else {
      this.subscriptionService.queryForOwnCompany().subscribe(this.onSuccess, this.onError);
    }
  }

  private onError() {
    return (error: HttpErrorResponse) => {
      this.subscriptions = [];
      console.error(error);
    };
  }

  private onSuccess() {
    return (response: HttpResponse<ISubscriptionPlan[]>) => {
      this.subscriptions = response.body!;
    };
  }
}
