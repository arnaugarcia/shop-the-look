import { NgModule } from '@angular/core';

import { CoreCommonModule } from '@core/common.module';

import { BreadcrumbModule } from 'app/layouts/content-header/breadcrumb/breadcrumb.module';
import { ContentHeaderComponent } from 'app/layouts/content-header/content-header.component';

@NgModule({
  declarations: [ContentHeaderComponent],
  imports: [CoreCommonModule, BreadcrumbModule],
  exports: [ContentHeaderComponent],
})
export class ContentHeaderModule {}
