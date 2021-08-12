export interface IPreferences {
  sku?: string;
}

export class Preferences implements IPreferences {
  constructor(public sku?: string) {}
}
