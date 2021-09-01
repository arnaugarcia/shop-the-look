import { Component } from '@angular/core';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'stl-publish',
  templateUrl: './publish.component.html',
  styleUrls: ['./publish.component.scss'],
})
export class PublishComponent {
  constructor(private studioService: StudioService) {
    this.studioService.navigate('publish');
  }
}
