import { IProduct } from '../../product/models/product.model';

export interface ICoordinate {
  x: number;
  y: number;
  product: IProduct;
  reference: string;
}
