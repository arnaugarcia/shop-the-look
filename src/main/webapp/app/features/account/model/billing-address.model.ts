export interface IBillingAddress {
  address?: string;
  country?: string;
  zipCode?: string;
  province?: string;
  city?: string;
}

export class BillingAddress implements IBillingAddress {
  constructor(public address?: string, public country?: string, public zipCode?: string, public city?: string, public province?: string) {}
}
