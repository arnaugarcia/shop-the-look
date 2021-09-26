import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';

@Component({
  selector: 'stl-list',
  templateUrl: './list.component.html',
  styleUrls: ['./list.component.scss'],
})
export class ListComponent {
  public spaces: ISpace[] = [];
  public contentHeader: ContentHeader;

  constructor(private route: ActivatedRoute) {
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
    this.route.data.subscribe(({ spaces }) => {
      this.spaces = spaces;
    });
  }
}
