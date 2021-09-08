export class PhotoRequest {
  constructor(public data: string, public photoContentType: string) {}
}

export interface IPhoto {
  url?: string;
  reference: string;
  format?: string;
}
