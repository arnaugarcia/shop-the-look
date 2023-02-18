import { Component, Input } from '@angular/core';

// Breadcrumb component interface
export interface Breadcrumb {
  type?: string;
  alignment?: string;
  links?: Array<{
    name: string;
    isLink: boolean;
    link?: string;
  }>;
}

@Component({
  selector: 'stl-breadcrumb',
  templateUrl: './breadcrumb.component.html',
})
export class BreadcrumbComponent {
  // input variable
  @Input() breadcrumb: Breadcrumb = {};
}
