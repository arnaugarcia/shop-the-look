import { ICompany } from 'app/entities/company/company.model';
import { ICoordinate } from 'app/entities/coordinate/coordinate.model';
import { ProductAvailability } from 'app/entities/enumerations/product-availability.model';

export interface IProduct {
  id?: number;
  sku?: string;
  name?: string;
  description?: string;
  link?: string;
  imageLink?: string;
  additionalImageLink?: string | null;
  availability?: ProductAvailability;
  price?: string;
  category?: string | null;
  company?: ICompany;
  coordinate?: ICoordinate | null;
}

export class Product implements IProduct {
  constructor(
    public id?: number,
    public sku?: string,
    public name?: string,
    public description?: string,
    public link?: string,
    public imageLink?: string,
    public additionalImageLink?: string | null,
    public availability?: ProductAvailability,
    public price?: string,
    public category?: string | null,
    public company?: ICompany,
    public coordinate?: ICoordinate | null
  ) {}
}

export class RawProduct implements IProduct {
  constructor(public sku: string, public name: string, public price: string) {}
}

export function getProductIdentifier(product: IProduct): number | undefined {
  return product.id;
}
