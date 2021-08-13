import { IProduct } from './product.model';

export class ImportProduct {
  constructor(public products: IProduct[], public update?: boolean, public companyReference?: string) {
    this.update = this.update ? this.update : false;
  }
}
