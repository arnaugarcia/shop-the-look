import { Component } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { ISpace } from '../../model/space.model';
import { StudioTemplate } from '../../store/models/state.model';
import { StudioStore } from '../../store/studio.store';

@Component({
  selector: 'stl-customize',
  templateUrl: './customize.component.html',
  styleUrls: ['./customize.component.scss'],
})
export class CustomizeComponent {
  public StudioTemplate = StudioTemplate;
  public space?: ISpace;

  constructor(public studioStore: StudioStore, private activatedRoute: ActivatedRoute) {
    this.studioStore.navigate('customize');
    this.activatedRoute.data.subscribe(({ space }) => {
      this.space = space;
    });
  }
}
