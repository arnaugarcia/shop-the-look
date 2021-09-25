import { Component } from '@angular/core';
import { StudioStore } from '../../store/studio.store';

@Component({
  selector: 'stl-enjoy',
  templateUrl: './enjoy.component.html',
  styleUrls: ['./enjoy.component.scss'],
})
export class EnjoyComponent {
  constructor(private studioStore: StudioStore) {
    studioStore.navigate('enjoy');
  }
}
