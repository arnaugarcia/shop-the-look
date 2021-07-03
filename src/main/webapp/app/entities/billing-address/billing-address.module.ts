import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { BillingAddressComponent } from './list/billing-address.component';
import { BillingAddressDetailComponent } from './detail/billing-address-detail.component';
import { BillingAddressUpdateComponent } from './update/billing-address-update.component';
import { BillingAddressDeleteDialogComponent } from './delete/billing-address-delete-dialog.component';
import { BillingAddressRoutingModule } from './route/billing-address-routing.module';

@NgModule({
  imports: [SharedModule, BillingAddressRoutingModule],
  declarations: [
    BillingAddressComponent,
    BillingAddressDetailComponent,
    BillingAddressUpdateComponent,
    BillingAddressDeleteDialogComponent,
  ],
  entryComponents: [BillingAddressDeleteDialogComponent],
})
export class BillingAddressModule {}
