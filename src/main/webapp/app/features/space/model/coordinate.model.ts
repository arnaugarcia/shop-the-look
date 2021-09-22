import { IProduct } from '../../product/models/product.model';

export interface ICoordinate {
  x: number;
  y: number;
  product: IProduct;
  reference?: string;
}

export class CoordinateRemoveRequest {
  constructor(private spaceReference: string, private coordinateReference: string, private photoReference: string) {}
}
