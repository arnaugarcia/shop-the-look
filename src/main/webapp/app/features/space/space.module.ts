import { NgModule } from '@angular/core';
import { SharedModule } from '../../shared/shared.module';
import { SpaceRoutingModule } from './space-routing.module';
import { StudioComponent } from './pages/studio/studio.component';
import { ContentHeaderModule } from '../../layouts/content-header/content-header.module';
import { CreateComponent } from './pages/create/create.component';
import { CustomizeComponent } from './pages/customize/customize.component';
import { PublishComponent } from './pages/publish/publish.component';
import { EnjoyComponent } from './pages/enjoy/enjoy.component';
import { TemplateComponent } from './pages/template/template.component';
import { ListComponent } from './pages/list/list.component';
import { EditComponent } from './pages/edit/edit.component';

@NgModule({
  declarations: [
    StudioComponent,
    CreateComponent,
    CustomizeComponent,
    PublishComponent,
    EnjoyComponent,
    TemplateComponent,
    ListComponent,
    EditComponent,
  ],
  imports: [SharedModule, SpaceRoutingModule, ContentHeaderModule],
})
export class SpaceModule {}
