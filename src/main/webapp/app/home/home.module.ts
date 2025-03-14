import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { SubscriptionWidgetComponent } from './widgets/subscription-widget/subscription-widget.component';
import { GeneralStatisticsWidgetComponent } from './widgets/general-statistics-widget/general-statistics-widget.component';
import { SpacesWidgetComponent } from './widgets/spaces-widget/spaces-widget.component';
import { TotalProductClicksComponent } from './widgets/total-product-clicks/total-product-clicks.component';
import { NumberThousandPipe } from './pipes/number-thousand.pipe';
import { TotalSpaceTimeComponent } from './widgets/total-space-time/total-space-time.component';
import { TotalSpaceViewsComponent } from './widgets/total-space-views/total-space-views.component';
import { TimeScalePipe } from './pipes/time-scale.pipe';
import { TopProductComponent } from './widgets/top-product/top-product.component';
import { TopSpaceComponent } from './widgets/top-space/top-space.component';
import { NgApexchartsModule } from 'ng-apexcharts';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE]), NgApexchartsModule],
  declarations: [
    HomeComponent,
    SubscriptionWidgetComponent,
    GeneralStatisticsWidgetComponent,
    SpacesWidgetComponent,
    TotalSpaceTimeComponent,
    TotalSpaceViewsComponent,
    TotalProductClicksComponent,
    NumberThousandPipe,
    TimeScalePipe,
    TopProductComponent,
    TopSpaceComponent,
  ],
})
export class HomeModule {}
