import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { AnalyticsRoutingModule } from './analytics-routing.module.route';
import { CoreCardModule } from '../../../@core/components/core-card/core-card.module';

@NgModule({
  declarations: [AnalyticsComponent],
  imports: [SharedModule, CoreCardModule, ContentHeaderModule, AnalyticsRoutingModule],
})
export class AnalyticsModule {}
