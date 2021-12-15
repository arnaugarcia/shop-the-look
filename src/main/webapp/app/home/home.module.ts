import { NgModule } from '@angular/core';
import { RouterModule } from '@angular/router';

import { SharedModule } from 'app/shared/shared.module';
import { HOME_ROUTE } from './home.route';
import { HomeComponent } from './home.component';
import { SubscriptionWidgetComponent } from './widgets/subscription-widget/subscription-widget.component';
import { GeneralStatisticsWidgetComponent } from './widgets/general-statistics-widget/general-statistics-widget.component';

@NgModule({
  imports: [SharedModule, RouterModule.forChild([HOME_ROUTE])],
  declarations: [HomeComponent, SubscriptionWidgetComponent, GeneralStatisticsWidgetComponent],
})
export class HomeModule {}
