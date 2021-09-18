import { Component, Input } from '@angular/core';
import { ICoordinate } from '../../model/coordinate.model';

@Component({
  selector: 'stl-product-coordinate-tooltip',
  templateUrl: './product-coordinate-tooltip.component.html',
  styleUrls: ['./product-coordinate-tooltip.component.scss'],
})
export class ProductCoordinateTooltipComponent {
  @Input()
  public coordinate!: ICoordinate;
}
