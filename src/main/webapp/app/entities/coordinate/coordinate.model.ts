import { IProduct } from 'app/entities/product/product.model';
import { IPhoto } from 'app/entities/photo/photo.model';

export interface ICoordinate {
  id?: number;
  x?: number | null;
  y?: number | null;
  products?: IProduct[];
  photo?: IPhoto | null;
}

export class Coordinate implements ICoordinate {
  constructor(
    public id?: number,
    public x?: number | null,
    public y?: number | null,
    public products?: IProduct[],
    public photo?: IPhoto | null
  ) {}
}

export function getCoordinateIdentifier(coordinate: ICoordinate): number | undefined {
  return coordinate.id;
}
