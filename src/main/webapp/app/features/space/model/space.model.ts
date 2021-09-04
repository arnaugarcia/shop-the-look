export class PhotoRequest {
  constructor(public data: any, public format: string) {}
}

export interface ISpace {
  name?: string;
  reference?: string;
  template?: string;
  description?: string | null;
}

export class Space implements ISpace {
  constructor(public name?: string, public reference?: string, public description?: string | null) {}
}

export class SpaceRequest {
  constructor(public name?: string, public description?: string | null) {}
}
