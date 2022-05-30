import { Component } from '@angular/core';
import { analyticsBreadcrumb } from '../analytics.breadcrumb';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

@Component({
  selector: 'stl-analytics',
  templateUrl: './analytics.component.html',
})
export class AnalyticsComponent {
  public contentHeader: ContentHeader = analyticsBreadcrumb;

  emittedEvents($event: any): void {
    console.error($event);
  }
}
