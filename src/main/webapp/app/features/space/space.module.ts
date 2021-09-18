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
import { SpacePhotoComponent } from './component/space-photo/space-photo.component';
import { FileUploadModule } from 'ng2-file-upload';
import { ProductSearchComponent } from './component/product-search/product-search.component';
import { ProductCoordinateComponent } from './component/product-coordinate/product-coordinate.component';
import { ProductCoordinateTooltipComponent } from './component/product-coordinate-tooltip/product-coordinate-tooltip.component';

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
    SpacePhotoComponent,
    ProductSearchComponent,
    ProductCoordinateComponent,
    ProductCoordinateTooltipComponent,
  ],
  imports: [SharedModule, SpaceRoutingModule, ContentHeaderModule, FileUploadModule],
})
export class SpaceModule {}
