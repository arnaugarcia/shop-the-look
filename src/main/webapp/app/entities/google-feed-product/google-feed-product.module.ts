import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { GoogleFeedProductComponent } from './list/google-feed-product.component';
import { GoogleFeedProductDetailComponent } from './detail/google-feed-product-detail.component';
import { GoogleFeedProductUpdateComponent } from './update/google-feed-product-update.component';
import { GoogleFeedProductDeleteDialogComponent } from './delete/google-feed-product-delete-dialog.component';
import { GoogleFeedProductRoutingModule } from './route/google-feed-product-routing.module';

@NgModule({
  imports: [SharedModule, GoogleFeedProductRoutingModule],
  declarations: [
    GoogleFeedProductComponent,
    GoogleFeedProductDetailComponent,
    GoogleFeedProductUpdateComponent,
    GoogleFeedProductDeleteDialogComponent,
  ],
  entryComponents: [GoogleFeedProductDeleteDialogComponent],
})
export class GoogleFeedProductModule {}
