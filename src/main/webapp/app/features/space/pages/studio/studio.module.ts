import { NgModule } from '@angular/core';
import { SharedModule } from '../../../../shared/shared.module';
import { StudioComponent } from './studio.component';

@NgModule({
  declarations: [StudioComponent],
  imports: [SharedModule],
  exports: [StudioComponent],
})
export class StudioModule {}
