import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { SpaceRoutingModule } from './space-routing.module';
import { StudioComponent } from './pages/studio/studio.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';

@NgModule({
  declarations: [StudioComponent],
  imports: [SharedModule, SpaceRoutingModule, ContentHeaderModule],
})
export class SpaceModule {}
