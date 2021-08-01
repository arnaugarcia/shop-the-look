import { NgModule } from '@angular/core';
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
    CoreCommonModule,
    CoreModule,
    FormsModule,
    NgbModule,
    InfiniteScrollModule,
    FontAwesomeModule,
    ReactiveFormsModule,
    TranslateModule,
  ],
})
export class SharedLibsModule {}
