import { ICompany } from '../../company/model/company.model';

export interface IProduct {
  sku?: string;
  name?: string;
  description?: string;
  reference?: string;
  link?: string;
  imageLink?: string;
  additionalImageLink?: string | null;
  price?: string;
  category?: string | null;
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
