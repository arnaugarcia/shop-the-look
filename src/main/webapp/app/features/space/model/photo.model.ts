export class PhotoRequest {
  constructor(public data: string, public photoContentType: string) {}
}

export interface IPhoto {
  link: string;
  reference: string;
  height: number;
  width: number;
}
