import { ICompany } from 'app/entities/company/company.model';

export interface IBillingAddress {
  id?: number;
  address?: string;
  city?: string;
  province?: string;
  zipCode?: string;
  country?: string;
  company?: ICompany | null;
}

export class BillingAddress implements IBillingAddress {
  constructor(
    public id?: number,
    public address?: string,
    public city?: string,
    public province?: string,
    public zipCode?: string,
    public country?: string,
    public company?: ICompany | null
  ) {}
}

export function getBillingAddressIdentifier(billingAddress: IBillingAddress): number | undefined {
  return billingAddress.id;
}
