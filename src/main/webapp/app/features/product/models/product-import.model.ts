import { IProduct } from './product.model';

export class ImportProduct {
  constructor(public products: IProduct[], public companyReference?: string) {}
}
