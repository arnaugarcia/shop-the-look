import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'stl-studio',
  templateUrl: './studio.component.html',
  styleUrls: ['./studio.component.scss'],
})
export class StudioComponent {
  public contentHeader: ContentHeader;

  constructor(public studioService: StudioService) {
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
