import { Component, Input } from '@angular/core';
import { ICoordinate } from '../../model/coordinate.model';

@Component({
  selector: 'stl-product-coordinate',
  templateUrl: './product-coordinate.component.html',
  styleUrls: ['./product-coordinate.component.scss'],
})
export class ProductCoordinateComponent {
  @Input()
  public coordinate!: ICoordinate;
  public visibleTooltip = false;
}
