export type ImportMethod = 'FEED' | 'CSV' | 'TSV';

export interface IPreferences {
  feedUrl?: string;
  importMethod?: ImportMethod;
  remainingImports?: number;
  lastImportBy?: string;
  lastImportTimestamp?: string;
}

export class PreferencesRequest implements IPreferences {
  constructor(public feedUrl?: string, public importMethod?: ImportMethod) {}
}
