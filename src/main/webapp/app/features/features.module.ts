import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  declarations: [],
  imports: [
    RouterModule.forChild([
      {
        path: 'products',
        data: { pageTitle: 'shopTheLookApp.photo.home.products' },
        loadChildren: () => import('./products/products.module').then(m => m.ProductsModule),
      },

      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class FeaturesModule {}
