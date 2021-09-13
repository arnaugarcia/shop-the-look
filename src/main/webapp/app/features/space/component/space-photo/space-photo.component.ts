import { Component, Input } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPhoto, PhotoRequest } from '../../model/photo.model';
import { SpacePhotoService } from '../../service/space-photo.service';
import { DataUtils } from '../../../../core/util/data-util.service';

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

  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });

  private fileReader = new FileReader();

  constructor(private spacePhotoService: SpacePhotoService, protected dataUtils: DataUtils) {
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
      throw new Error('No space reference was specified for this photo');
    }
    this.fileReader.onloadend = () => {
      this.uploadFile(droopedFile);
    };
    this.fileReader.readAsDataURL(this.uploader.queue[0]._file);
  }

  addCoordinate($event: any): void {
    console.error($event.layerX, $event.layerY);
  }

  public deletePhoto(photoReference: string): void {
    this.spacePhotoService.removePhoto(this.spaceReference!, photoReference).subscribe(
      (response: HttpResponse<any>) => this.onUploadSuccess(response),
      (error: HttpErrorResponse) => this.onUploadError(error)
    );
  }

  private uploadFile(droopedFile: any): void {
    if (!this.fileReader.result) {
      throw Error('The reader has not loaded any file');
    }
    this.spacePhotoService
      .addPhoto(this.spaceReference!, new PhotoRequest(this.fileReader.result.toString().split(',')[1], droopedFile.type))
      .subscribe(
        (response: HttpResponse<IPhoto>) => this.onUploadSuccess(response),
        (error: HttpErrorResponse) => this.onUploadError(error)
      );
  }

  private onUploadSuccess(response: HttpResponse<IPhoto>): void {
    this.loading = false;
    this.photo = response.body!;
  }

  private onUploadError(error: HttpErrorResponse): void {
    console.error(error);
  }
}
