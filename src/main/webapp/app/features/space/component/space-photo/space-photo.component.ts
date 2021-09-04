import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'stl-space-photo',
  templateUrl: './space-photo.component.html',
  styleUrls: ['./space-photo.component.scss'],
})
export class SpacePhotoComponent {
  public hasBaseDropZoneOver = false;
  public loading = false;
  public error = false;

  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });

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
