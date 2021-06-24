import { NgModule } from '@angular/core';
import { SharedModule } from 'app/shared/shared.module';
import { CoordinateComponent } from './list/coordinate.component';
import { CoordinateDetailComponent } from './detail/coordinate-detail.component';
import { CoordinateUpdateComponent } from './update/coordinate-update.component';
import { CoordinateDeleteDialogComponent } from './delete/coordinate-delete-dialog.component';
import { CoordinateRoutingModule } from './route/coordinate-routing.module';

@NgModule({
  imports: [SharedModule, CoordinateRoutingModule],
  declarations: [CoordinateComponent, CoordinateDetailComponent, CoordinateUpdateComponent, CoordinateDeleteDialogComponent],
  entryComponents: [CoordinateDeleteDialogComponent],
})
export class CoordinateModule {}
