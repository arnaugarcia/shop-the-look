import { Component, OnDestroy, OnInit } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { StudioStore } from '../../store/studio.store';
import { CurrentStep } from '../../store/models/state.model';
import { Subscription } from 'rxjs';

@Component({
  selector: 'stl-studio',
  templateUrl: './studio.component.html',
  styleUrls: ['./studio.component.scss'],
})
export class StudioComponent implements OnInit, OnDestroy {
  public contentHeader: ContentHeader;
  public currentStep: CurrentStep = 'create';
  private stepSubscription = new Subscription();

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

  ngOnInit(): void {
    this.stepSubscription = this.studioStore.currentStep$.subscribe((currentStep: CurrentStep) => (this.currentStep = currentStep));
  }

  ngOnDestroy(): void {
    this.stepSubscription.unsubscribe();
  }
}
