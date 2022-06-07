import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { AnalyticsComponent } from './pages/analytics/analytics.component';
import { AnalyticsRoutingModule } from './analytics-routing.module.route';
import { CoreCardModule } from '../../../@core/components/core-card/core-card.module';
import { SpacesViewClicksComponent } from './pages/analytics/widgets/spaces-view-clicks/spaces-view-clicks.component';
import { Ng2FlatpickrModule } from 'ng2-flatpickr';

@NgModule({
  declarations: [AnalyticsComponent, SpacesViewClicksComponent],
  imports: [SharedModule, CoreCardModule, ContentHeaderModule, AnalyticsRoutingModule, Ng2FlatpickrModule],
})
export class AnalyticsModule {}
