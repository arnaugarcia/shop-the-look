export interface ISubscriptionPlan {
  name: string;
  description?: string;
  reference: string;
  popular: boolean;
  price: number;
  benefits: ISubscriptionBenefits;
  current: boolean;
  custom: boolean;
}

export interface ISubscriptionBenefits {
  products: number;
  spaces: number;
  requests: number;
}
