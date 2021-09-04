import { Component } from '@angular/core';
import { StudioService, StudioTemplate } from '../../service/studio.service';

@Component({
  selector: 'stl-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.scss'],
})
export class CustomizeComponent {
  public StudioTemplate = StudioTemplate;

  constructor(public studioService: StudioService) {
    this.studioService.navigate('customize');
  }
}
