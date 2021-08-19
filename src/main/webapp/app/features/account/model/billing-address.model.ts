export interface IBillingAddress {
  country?: string;
  zipCode?: string;
  province?: string;
  city?: string;
}

export class BillingAddress implements IBillingAddress {
  constructor(public country?: string, public zipCode?: string, public city?: string, public province?: string) {}
}
