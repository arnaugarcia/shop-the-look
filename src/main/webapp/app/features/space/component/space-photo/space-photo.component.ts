import { Component, ElementRef, Input, OnInit, ViewChild } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { HttpErrorResponse, HttpResponse } from '@angular/common/http';
import { IPhoto, PhotoRequest } from '../../model/photo.model';
import { SpacePhotoService } from '../../service/space-photo.service';
import { ProductSearchComponent } from '../product-search/product-search.component';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { SpaceCoordinateService } from '../../service/space-coordinate.service';
import { IProduct } from '../../../product/models/product.model';
import { CoordinateCreateRequest, ICoordinate } from '../../model/coordinate.model';
import { AlertService } from '../../../../core/util/alert.service';
import { Alert } from 'selenium-webdriver';

@Component({
  selector: 'stl-space-photo',
  templateUrl: './space-photo.component.html',
  styleUrls: ['./space-photo.component.scss'],
})
export class SpacePhotoComponent implements OnInit {
  @Input()
  public spaceReference?: string;

  @Input()
  public photo?: IPhoto;

  public coordinates: ICoordinate[] = [];

  public hasBaseDropZoneOver = false;
  public loading = false;
  public error = false;

  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });

  @ViewChild('photoCoordinates', { static: false, read: ElementRef })
  private photoCoordinates!: ElementRef;

  private fileReader = new FileReader();

  constructor(
    private spacePhotoService: SpacePhotoService,
    private alertService: AlertService,
    private spaceCoordinateService: SpaceCoordinateService,
    private modalService: NgbModal
  ) {}

  ngOnInit(): void {
    this.coordinates = this.photo!.coordinates!;
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
      this.alertService.addAlert({ type: 'danger', message: 'Something went wrong with the upload' });
      throw new Error('No space reference was specified for this photo');
    }
    this.fileReader.onloadend = () => {
      this.uploadFile(droopedFile);
    };
    this.fileReader.readAsDataURL(this.uploader.queue[0]._file);
  }

  addCoordinate($event: any): void {
    const ngbModalRef = this.modalService.open(ProductSearchComponent, {
      centered: true,
      size: 'lg',
    });

    ngbModalRef.result
      .then(
        (product: IProduct) =>
          new CoordinateCreateRequest(product.reference!, this.photo!.reference, Number($event.layerX), Number($event.layerY))
      )
      .then((request: CoordinateCreateRequest) => {
        this.spaceCoordinateService.addCoordinate(this.spaceReference!, request).subscribe((response: HttpResponse<ICoordinate>) => {
          const coordinate = response.body!;
          this.coordinates.push({ x: coordinate.x, y: coordinate.y, product: coordinate.product, reference: coordinate.reference });
        });
      })
      .catch((result: any) => void result);
  }

  public deletePhoto(photoReference: string): void {
    this.spacePhotoService.removePhoto(this.spaceReference!, photoReference).subscribe(
      (response: HttpResponse<any>) => this.onUploadSuccess(response),
      (error: HttpErrorResponse) => this.onUploadError(error)
    );
  }

  public removeCoordinate(coordinate: ICoordinate): void {
    this.spaceCoordinateService.removeCoordinate(this.spaceReference!, coordinate.reference!).subscribe(() => {
      const index = this.coordinates.indexOf(coordinate);
      if (index > -1) {
        this.coordinates.splice(index, 1);
      }
    });
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
