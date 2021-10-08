export interface ISubscriptionPlan {
  name: string;
  description?: string | null;
  reference: string;
  popular: boolean;
  price: number;
  benefits: string[];
  current: boolean;
}
