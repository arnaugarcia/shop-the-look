import { Component, Input } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPhoto, PhotoRequest } from '../../model/photo.model';
import { SpacePhotoService } from '../../service/space-photo.service';

@Component({
  selector: 'stl-space-photo',
  templateUrl: './space-photo.component.html',
  styleUrls: ['./space-photo.component.scss'],
})
export class SpacePhotoComponent {
  @Input()
  public spaceReference?: string;

  @Input()
  public height: string;

  @Input()
  public width: string;

  @Input()
  public photo?: IPhoto;

  public hasBaseDropZoneOver = false;
  public loading = false;
  public error = false;

  public photoUrl?: string = 'https://images.unsplash.com/photo-1630688231126-dd36840fce51';

  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });

  private fileReader = new FileReader();

  constructor(private photoService: SpacePhotoService) {
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
    if (!droopedFile.type.includes('image/')) {
      this.error = true;
      return;
    }
    if (!this.spaceReference) {
      throw new Error('No reference was specified for this space');
    }
    this.fileReader.onloadend = () => {
      this.uploadFile(droopedFile);
    };
    this.fileReader.readAsDataURL(this.uploader.queue[0]._file);
  }

  public deletePhoto(photoReference: string): void {
    this.photoService.removePhoto(this.spaceReference!, photoReference).subscribe(
      (response: HttpResponse<any>) => this.onUploadSuccess(response),
      (error: HttpErrorResponse) => this.onUploadError(error)
    );
  }

  private uploadFile(droopedFile: any): void {
    this.photoService.addPhoto(this.spaceReference!, new PhotoRequest(this.fileReader.result, droopedFile.type)).subscribe(
      (response: HttpResponse<any>) => this.onUploadSuccess(response),
      (error: HttpErrorResponse) => this.onUploadError(error)
    );
  }

  private onUploadSuccess(response: HttpResponse<any>): void {
    console.error(response);
  }

  private onUploadError(error: HttpErrorResponse): void {
    console.error(error);
  }
}
