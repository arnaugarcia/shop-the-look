import { NgModule } from '@angular/core';

import { CoreCommonModule } from '@core/common.module';

import { ContentComponent } from 'app/layouts/content/content.component';

@NgModule({
  declarations: [ContentComponent],
  imports: [CoreCommonModule],
  exports: [ContentComponent],
})
export class ContentModule {}
