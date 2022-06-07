import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { AnalyticsRoutingModule } from './analytics-routing.module.route';
import { CoreCardModule } from '../../../@core/components/core-card/core-card.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import { SpacesViewClicksComponent } from './pages/analytics/widgets/spaces-view-clicks/spaces-view-clicks.component';
import { Ng2FlatpickrModule } from 'ng2-flatpickr';
import { ProductClicksComponent } from './pages/analytics/widgets/product-clicks/product-clicks.component';

@NgModule({
  declarations: [AnalyticsComponent, SpacesViewClicksComponent, ProductClicksComponent],
  imports: [SharedModule, CoreCardModule, ContentHeaderModule, AnalyticsRoutingModule, NgApexchartsModule, Ng2FlatpickrModule],
})
export class AnalyticsModule {}
