import { Component } from '@angular/core';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'stl-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.scss'],
})
export class CustomizeComponent {
  constructor(private studioService: StudioService) {
    this.studioService.navigate('customize');
  }
}
