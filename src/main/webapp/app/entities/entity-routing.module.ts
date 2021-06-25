import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

@NgModule({
  imports: [
    RouterModule.forChild([
      {
        path: 'company',
        data: { pageTitle: 'shopTheLookApp.company.home.title' },
        loadChildren: () => import('./company/company.module').then(m => m.CompanyModule),
      },
      {
        path: 'space',
        data: { pageTitle: 'shopTheLookApp.space.home.title' },
        loadChildren: () => import('./space/space.module').then(m => m.SpaceModule),
      },
      {
        path: 'space-template',
        data: { pageTitle: 'shopTheLookApp.spaceTemplate.home.title' },
        loadChildren: () => import('./space-template/space-template.module').then(m => m.SpaceTemplateModule),
      },
      {
        path: 'photo',
        data: { pageTitle: 'shopTheLookApp.photo.home.title' },
        loadChildren: () => import('./photo/photo.module').then(m => m.PhotoModule),
      },
      {
        path: 'subscription-plan',
        data: { pageTitle: 'shopTheLookApp.subscriptionPlan.home.title' },
        loadChildren: () => import('./subscription-plan/subscription-plan.module').then(m => m.SubscriptionPlanModule),
      },
      {
        path: 'product',
        data: { pageTitle: 'shopTheLookApp.product.home.title' },
        loadChildren: () => import('./product/product.module').then(m => m.ProductModule),
      },
      {
        path: 'google-feed-product',
        data: { pageTitle: 'shopTheLookApp.googleFeedProduct.home.title' },
        loadChildren: () => import('./google-feed-product/google-feed-product.module').then(m => m.GoogleFeedProductModule),
      },
      {
        path: 'coordinate',
        data: { pageTitle: 'shopTheLookApp.coordinate.home.title' },
        loadChildren: () => import('./coordinate/coordinate.module').then(m => m.CoordinateModule),
      },
      /* jhipster-needle-add-entity-route - JHipster will add entity modules routes here */
    ]),
  ],
})
export class EntityRoutingModule {}
