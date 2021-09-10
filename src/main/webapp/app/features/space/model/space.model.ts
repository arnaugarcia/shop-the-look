import { IPhoto } from './photo.model';

export interface ISpace {
  name: string;
  reference: string;
  template: string;
  description?: string | null;
  photos: IPhoto[];
}

export class Space {
  constructor(public name: string, public reference: string, public description?: string | null) {}
}

export class SpaceRequest {
  constructor(public name: string, public description?: string | null) {}
}
