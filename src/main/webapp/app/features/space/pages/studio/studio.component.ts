import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

@Component({
  selector: 'stl-studio',
  templateUrl: './studio.component.html',
  styleUrls: ['./studio.component.scss'],
})
export class StudioComponent {
  public contentHeader: ContentHeader;

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
            isLink: true,
            link: '/spaces',
          },
          {
            name: 'Space Studio',
            isLink: false,
          },
        ],
      },
    };
  }
}
