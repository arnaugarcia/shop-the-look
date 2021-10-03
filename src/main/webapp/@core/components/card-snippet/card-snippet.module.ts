import { NgModule } from '@angular/core';
import { HIGHLIGHT_OPTIONS, HighlightModule } from 'ngx-highlightjs';

import { CoreCommonModule } from '@core/common.module';
import { CoreCardSnippetComponent } from '@core/components/card-snippet/card-snippet.component';
import { SharedModule } from '../../../app/shared/shared.module';
import { NgbModule } from '@ng-bootstrap/ng-bootstrap';

@NgModule({
  declarations: [CoreCardSnippetComponent],
  imports: [SharedModule, NgbModule, HighlightModule, CoreCommonModule],
  providers: [
    {
      provide: HIGHLIGHT_OPTIONS,
      useValue: {
        coreLibraryLoader: () => import('highlight.js/lib/core'),
        languages: {
          typescript: () => import('highlight.js/lib/languages/typescript'),
        },
      },
    },
  ],
  exports: [CoreCardSnippetComponent],
})
export class CardSnippetModule {}
