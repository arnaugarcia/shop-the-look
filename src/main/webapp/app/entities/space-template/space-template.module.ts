import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { SpaceTemplateComponent } from './list/space-template.component';
import { SpaceTemplateDetailComponent } from './detail/space-template-detail.component';
import { SpaceTemplateUpdateComponent } from './update/space-template-update.component';
import { SpaceTemplateDeleteDialogComponent } from './delete/space-template-delete-dialog.component';
import { SpaceTemplateRoutingModule } from './route/space-template-routing.module';

@NgModule({
  imports: [SharedModule, SpaceTemplateRoutingModule],
  declarations: [SpaceTemplateComponent, SpaceTemplateDetailComponent, SpaceTemplateUpdateComponent, SpaceTemplateDeleteDialogComponent],
  entryComponents: [SpaceTemplateDeleteDialogComponent],
})
export class SpaceTemplateModule {}
