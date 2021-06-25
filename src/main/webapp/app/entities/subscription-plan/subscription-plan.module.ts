import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SubscriptionPlanComponent } from './list/subscription-plan.component';
import { SubscriptionPlanDetailComponent } from './detail/subscription-plan-detail.component';
import { SubscriptionPlanUpdateComponent } from './update/subscription-plan-update.component';
import { SubscriptionPlanDeleteDialogComponent } from './delete/subscription-plan-delete-dialog.component';
import { SubscriptionPlanRoutingModule } from './route/subscription-plan-routing.module';

@NgModule({
  imports: [SharedModule, SubscriptionPlanRoutingModule],
  declarations: [
    SubscriptionPlanComponent,
    SubscriptionPlanDetailComponent,
    SubscriptionPlanUpdateComponent,
    SubscriptionPlanDeleteDialogComponent,
  ],
  entryComponents: [SubscriptionPlanDeleteDialogComponent],
})
export class SubscriptionPlanModule {}
