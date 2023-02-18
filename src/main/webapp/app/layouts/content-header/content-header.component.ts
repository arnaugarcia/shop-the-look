import { Component, Input } from '@angular/core';
import { Breadcrumb } from './breadcrumb/breadcrumb.component';
import { Action } from './action/action.component';

export interface ContentHeader {
  headerTitle: string;
  actions?: Action[];
  breadcrumb?: Breadcrumb;
}

@Component({
  selector: 'stl-content-header',
  templateUrl: './content-header.component.html',
})
export class ContentHeaderComponent {
  // input variable
  @Input() contentHeader: ContentHeader | undefined;
}
