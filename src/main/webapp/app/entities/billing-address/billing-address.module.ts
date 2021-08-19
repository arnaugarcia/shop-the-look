import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BillingAddressDetailComponent } from './detail/billing-address-detail.component';
import { BillingAddressUpdateComponent } from './update/billing-address-update.component';
import { BillingAddressRoutingModule } from './route/billing-address-routing.module';

@NgModule({
  imports: [SharedModule, BillingAddressRoutingModule],
  declarations: [BillingAddressDetailComponent, BillingAddressUpdateComponent],
})
export class BillingAddressModule {}
