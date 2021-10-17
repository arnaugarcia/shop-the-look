import { Component, Inject, OnInit } from '@angular/core';
import { ISubscriptionPlan } from '../../model/subscription-plan.model';
import { ActivatedRoute } from '@angular/router';
import { SubscriptionService } from '../../service/subscription.service';
import { AccountService } from '../../../../core/auth/account.service';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { ICheckoutData } from '../../model/checkout.model';
import { DOCUMENT } from '@angular/common';

@Component({
  selector: 'stl-subscription',
  templateUrl: './subscription.component.html',
  styleUrls: ['./subscription.component.scss'],
})
export class SubscriptionComponent implements OnInit {
  public subscriptions: ISubscriptionPlan[] = [];
  private companyReference?: string;

  constructor(
    private route: ActivatedRoute,
    private accountService: AccountService,
    private subscriptionService: SubscriptionService,
    @Inject(DOCUMENT) private document: Document
  ) {}

  ngOnInit(): void {
    this.route.data.subscribe(({ subscriptions }) => {
      this.subscriptions = subscriptions;
    });
    this.companyReference = this.route.parent?.snapshot.params.reference;
  }

  selectedPlan(selectedPlan: ISubscriptionPlan): void {
    if (this.accountService.isAdmin()) {
      this.subscriptionService.updateForCompany(this.companyReference!, selectedPlan.reference).subscribe(() => {
        this.loadAll();
      }, this.onError());
    } else {
      if (selectedPlan.custom) {
        window.open('mailto:contact@weareklai.com?subject=[Shop The Look] - Custom pricing', '_blank');
      } else {
        this.subscriptionService.checkoutSubscription(selectedPlan.reference).subscribe((response: HttpResponse<ICheckoutData>) => {
          this.document.location.href = response.body!.checkoutUrl;
        }, this.onError());
      }
    }
  }
  private loadAll(): void {
    if (this.companyReference) {
      this.subscriptionService.queryForCompany(this.companyReference).subscribe(this.onSuccess(), this.onError());
    } else {
      this.subscriptionService.queryForOwnCompany().subscribe(this.onSuccess(), this.onError());
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
