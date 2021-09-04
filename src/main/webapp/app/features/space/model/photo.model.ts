export class PhotoRequest {
  constructor(public data: any, public format: string) {}
}

export interface IPhoto {
  url?: string;
  reference: string;
  format?: string;
}
