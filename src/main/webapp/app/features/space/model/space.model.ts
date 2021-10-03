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

export class EmptySpace implements ISpace {
  constructor(public name = '', public reference = '', public description = '', public template = '', public photos = []) {}
}

export class SpaceRequest {
  constructor(public name?: string, public template?: string, public description?: string | null) {}
}
