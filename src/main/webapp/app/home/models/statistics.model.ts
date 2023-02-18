export interface SubscriptionStatus {
  companyName: string;
  name: string;
  description: string;
  price: number;
}

export interface GeneralStatus {
  totalSpaces: number;
  totalProducts: number;
  totalPhotos: number;
  totalEmployees: number;
}

export interface Space {
  name: string;
  description: string;
  reference: string;
  photos: number;
  coordinates: number;
}
