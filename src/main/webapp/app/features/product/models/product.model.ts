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
    public description?: string,
    public link?: string,
    public imageLink?: string,
    public additionalImageLink?: string | null,
    public price?: string,
    public category?: string | null,
    public company?: ICompany
  ) {}
}

export class RawProduct implements IProduct {
  constructor(public sku: string, public name: string, public description: string, public link: string, public price: string) {}
}
