import { Component } from '@angular/core';
import { StudioStore } from '../../store/studio.store';

@Component({
  selector: 'stl-publish',
  templateUrl: './publish.component.html',
  styleUrls: ['./publish.component.scss'],
})
export class PublishComponent {
  constructor(private studioStore: StudioStore) {
    this.studioStore.navigate('publish');
  }
}
