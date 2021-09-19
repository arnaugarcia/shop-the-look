import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { StudioStore } from '../../store/studio.store';
import { Observable } from 'rxjs';
import { CurrentStep } from '../../store/models/state.model';

@Component({
  selector: 'stl-studio',
  templateUrl: './studio.component.html',
  styleUrls: ['./studio.component.scss'],
})
export class StudioComponent {
  public contentHeader: ContentHeader;
  public currentStep$: Observable<CurrentStep> = this.studioStore.currentStep$;

  constructor(private studioStore: StudioStore) {
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
