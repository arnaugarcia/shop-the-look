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
        path: 'space-template',
        data: { pageTitle: 'shopTheLookApp.spaceTemplate.home.title' },
        loadChildren: () => import('./space-template/space-template.module').then(m => m.SpaceTemplateModule),
      },
      {
        path: 'subscription-plan',
        data: { pageTitle: 'shopTheLookApp.subscriptionPlan.home.title' },
        loadChildren: () => import('./subscription-plan/subscription-plan.module').then(m => m.SubscriptionPlanModule),
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
