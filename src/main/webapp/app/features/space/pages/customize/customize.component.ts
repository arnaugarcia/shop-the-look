import { Component } from '@angular/core';
import { StudioService, StudioTemplate } from '../../service/studio.service';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';

@Component({
  selector: 'stl-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.scss'],
})
export class CustomizeComponent {
  public StudioTemplate = StudioTemplate;
  public space?: ISpace;

  constructor(public studioService: StudioService, private activatedRoute: ActivatedRoute) {
    this.studioService.navigate('customize');
    this.activatedRoute.data.subscribe(({ space }) => {
      this.space = space;
    });
  }
}
