import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'account',
        // data: { pageTitle: 'shopTheLookApp.photo.home.product' },
        loadChildren: () => import('./account/account.module').then(m => m.AccountModule),
      },
    ]),
  ],
})
export class FeaturesModule {}
