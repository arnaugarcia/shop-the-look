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
      this.subscriptionService.updateForCompany(this.companyReference!, selectedPlan.reference).subscribe(
        () => this.loadAll(),
        (error: HttpErrorResponse) => this.onError(error)
      );
    } else {
      if (selectedPlan.custom) {
        window.open('mailto:contact@weareklai.com?subject=[Shop The Look] - Custom pricing', '_blank');
      } else {
        this.subscriptionService.checkoutSubscription(selectedPlan.reference).subscribe(
          (response: HttpResponse<ICheckoutData>) => {
            this.document.location.href = response.body!.checkoutUrl;
          },
          (error: HttpErrorResponse) => this.onError(error)
        );
      }
    }
  }
  private loadAll(): void {
    if (this.companyReference) {
      this.subscriptionService.queryForCompany(this.companyReference).subscribe(
        (response: HttpResponse<ISubscriptionPlan[]>) => this.onSuccess(response),
        (error: HttpErrorResponse) => this.onError(error)
      );
    } else {
      this.subscriptionService.queryForOwnCompany().subscribe(
        (response: HttpResponse<ISubscriptionPlan[]>) => this.onSuccess(response),
        (error: HttpErrorResponse) => this.onError(error)
      );
    }
  }

  private onError(error: HttpErrorResponse): void {
    this.subscriptions = [];
    console.error(error);
  }

  private onSuccess(response: HttpResponse<ISubscriptionPlan[]>): void {
    this.subscriptions = response.body!;
  }
}
