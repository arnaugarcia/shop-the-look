import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { SpaceRoutingModule } from './space-routing.module';
import { StudioModule } from './pages/studio/studio.module';

@NgModule({
  declarations: [],
  imports: [SharedModule, StudioModule, SpaceRoutingModule],
})
export class SpaceModule {}
