import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { AnalyticsRoutingModule } from './analytics-routing.module.route';
import { CoreCardModule } from '../../../@core/components/core-card/core-card.module';
import { NgApexchartsModule } from 'ng-apexcharts';
import { SpacesViewClicksComponent } from './pages/analytics/widgets/spaces-view-clicks/spaces-view-clicks.component';

@NgModule({
  declarations: [AnalyticsComponent, SpacesViewClicksComponent],
  imports: [SharedModule, CoreCardModule, ContentHeaderModule, AnalyticsRoutingModule, NgApexchartsModule],
})
export class AnalyticsModule {}
