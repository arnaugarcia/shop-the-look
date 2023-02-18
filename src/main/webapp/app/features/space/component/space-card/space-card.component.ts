import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ISpace } from '../../model/space.model';

@Component({
  selector: 'stl-space-card',
  templateUrl: './space-card.component.html',
  styleUrls: ['./space-card.component.scss'],
})
export class SpaceCardComponent {
  @Input()
  public space?: ISpace;

  @Output()
  public removeClick = new EventEmitter<ISpace>();

  @Output()
  public editClick = new EventEmitter<ISpace>();
}
