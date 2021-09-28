import { NgModule } from '@angular/core';

import { CoreCommonModule } from '@core/common.module';

import { BreadcrumbModule } from 'app/layouts/content-header/breadcrumb/breadcrumb.module';
import { ContentHeaderComponent } from 'app/layouts/content-header/content-header.component';
import { ActionComponent } from './action/action.component';

@NgModule({
  declarations: [ContentHeaderComponent, ActionComponent],
  imports: [CoreCommonModule, BreadcrumbModule],
  exports: [ContentHeaderComponent],
})
export class ContentHeaderModule {}
