import { ICompany } from '../../company/model/company.model';

export interface IProduct {
  sku?: string;
  name?: string;
  reference?: string;
  link?: string;
  price?: string;
  companyReference?: string;
}

export class Product implements IProduct {
  constructor(
    public sku?: string,
    public name?: string,
    public reference?: string,
    public link?: string,
    public price?: string,
    public company?: ICompany
  ) {}
}

export class ProductImport implements IProduct {
  constructor(public sku: string, public name: string, public link: string, public price: string) {}

  isValid(): boolean {
    return this.sku !== '' && this.name !== '' && this.link !== '' && this.price !== '';
  }
}
