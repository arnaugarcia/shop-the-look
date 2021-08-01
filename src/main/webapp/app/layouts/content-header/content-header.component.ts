import { Component, Input } from '@angular/core';
import { Breadcrumb } from './breadcrumb/breadcrumb.component';

// ContentHeader component interface
export interface ContentHeader {
  headerTitle: string;
  actionButton: boolean;
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
