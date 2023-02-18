import { IProduct } from '../../product/models/product.model';

export interface ICoordinate {
  x: number;
  y: number;
  product: IProduct;
  reference?: string;
}

export class CoordinateCreateRequest {
  constructor(public productReference: string, public photoReference: string, public x: number, public y: number) {}
}
