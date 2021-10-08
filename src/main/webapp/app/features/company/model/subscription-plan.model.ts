export interface ISubscriptionPlan {
  name?: string;
  description?: string | null;
  popular?: string;
  price?: number;
  benefits?: string[];
  current?: boolean;
}
