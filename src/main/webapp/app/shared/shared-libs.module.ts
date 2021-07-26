import { NgModule } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule, ReactiveFormsModule } from '@angular/forms';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';
import { InfiniteScrollModule } from 'ngx-infinite-scroll';
import { FontAwesomeModule } from '@fortawesome/angular-fontawesome';
import { TranslateModule } from '@ngx-translate/core';
import { CoreCommonModule } from '../../@core/common.module';
import { CoreModule } from '../../@core/core.module';
import { coreConfig } from '../app-config';

@NgModule({
  imports: [CoreModule.forRoot(coreConfig)],
  exports: [
    FormsModule,
    CommonModule,
    NgbModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TranslateModule,
    CoreModule,
    CoreCommonModule,
  ],
})
export class SharedLibsModule {}
