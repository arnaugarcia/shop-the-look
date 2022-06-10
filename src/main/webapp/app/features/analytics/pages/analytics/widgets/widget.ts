import { IAnalyticsCriteria } from '../../../models/analytics-criteria.model';
import { OnInit } from '@angular/core';

export interface AnalyticsWidget extends OnInit {
  loadAll(criteria?: IAnalyticsCriteria): void;
}
