import { ModuleWithProviders, NgModule } from '@angular/core';

import { CORE_CUSTOM_CONFIG } from '@core/services/config.service';
import { CoreConfig } from './types';

@NgModule()
export class CoreModule {
  /* constructor(@Optional() @SkipSelf() parentModule: CoreModule) {
    if (parentModule) {
      throw new Error('Import CoreModule in the AppModule only');
    }
  } */

  static forRoot(config: CoreConfig): ModuleWithProviders<CoreModule> {
    return {
      ngModule: CoreModule,
      providers: [
        {
          provide: CORE_CUSTOM_CONFIG,
          useValue: config,
        },
      ],
    };
  }
}
