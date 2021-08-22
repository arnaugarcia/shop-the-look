export type ImportMethod = 'FEED' | 'CSV' | 'TSV';

export interface IPreferences {
  feedUrl?: string;
  importMethod?: ImportMethod;
}

export class Preferences implements IPreferences {
  constructor(public feedUrl?: string, public importMethod?: ImportMethod) {}
}
