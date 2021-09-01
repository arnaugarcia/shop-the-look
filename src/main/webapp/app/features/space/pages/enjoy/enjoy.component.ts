import { Component } from '@angular/core';
import { StudioService } from '../../service/studio.service';

@Component({
  selector: 'stl-enjoy',
  templateUrl: './enjoy.component.html',
  styleUrls: ['./enjoy.component.scss'],
})
export class EnjoyComponent {
  constructor(private studioService: StudioService) {
    this.studioService.navigate('enjoy');
  }
}
