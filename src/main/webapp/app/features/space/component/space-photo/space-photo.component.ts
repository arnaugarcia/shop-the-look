import { Component, Input } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { SpaceService } from '../../service/space.service';

@Component({
  selector: 'stl-space-photo',
  templateUrl: './space-photo.component.html',
  styleUrls: ['./space-photo.component.scss'],
})
export class SpacePhotoComponent {
  @Input()
  public height: string;

  @Input()
  public width: string;

  public hasBaseDropZoneOver = false;
  public loading = false;
  public error = false;

  public photoUrl =
    'https://images.unsplash.com/photo-1630688231126-dd36840fce51?ixid=MnwxMjA3fDB8MHxwaG90by1wYWdlfHx8fGVufDB8fHx8&ixlib=rb-1.2.1&auto=format&fit=crop&w=701&q=80';

  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });

  constructor(private spaceService: SpaceService) {
    this.height = 'auto';
    this.width = 'auto';
  }

  fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  onFileDropped($event: any): void {
    this.loading = true;
    this.error = false;
    const droopedFile = $event[0];
    if (droopedFile.type !== 'image/png') {
      this.error = true;
      return;
    }
  }
}
