import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { SubscriptionWidgetComponent } from './widgets/subscription-widget/subscription-widget.component';
import { GeneralStatisticsWidgetComponent } from './widgets/general-statistics-widget/general-statistics-widget.component';
import { SpacesWidgetComponent } from './widgets/spaces-widget/spaces-widget.component';
import { TotalSpaceViewsComponent } from './widgets/total-space-views/total-space-views.component';
import { TotalProductClicksComponent } from './widgets/total-product-clicks/total-product-clicks.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [
    HomeComponent,
    SubscriptionWidgetComponent,
    GeneralStatisticsWidgetComponent,
    SpacesWidgetComponent,
    TotalSpaceViewsComponent,
    TotalProductClicksComponent,
  ],
})
export class HomeModule {}
