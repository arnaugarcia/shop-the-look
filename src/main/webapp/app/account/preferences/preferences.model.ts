export interface IPreferences {
  feedUrl?: string;
  importMethod?: ImportMethod;
}

export enum ImportMethod {
  CSV = 'CSV',

  TSV = 'TSV',

  FEED = 'FEED',
}

export class Preferences implements IPreferences {
  constructor(public feedUrl?: string, public importMethod?: ImportMethod) {}
}
