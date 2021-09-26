import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

@Component({
  selector: 'stl-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent {
  contentHeader: ContentHeader;

  constructor() {
    this.contentHeader = {
      headerTitle: 'Spaces',
      actionButton: true,
      breadcrumb: {
        type: '',
        links: [
          {
            name: 'Home',
            isLink: true,
            link: '/',
          },
          {
            name: 'Spaces',
            isLink: false,
          },
        ],
      },
    };
  }
}
