import { Component, ViewEncapsulation } from '@angular/core';
import { analyticsBreadcrumb } from '../analytics.breadcrumb';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

@Component({
  selector: 'stl-analytics',
  styleUrls: ['./analytics.component.scss'],
  templateUrl: './analytics.component.html',
  encapsulation: ViewEncapsulation.None,
})
export class AnalyticsComponent {
  public contentHeader: ContentHeader = analyticsBreadcrumb;
}
