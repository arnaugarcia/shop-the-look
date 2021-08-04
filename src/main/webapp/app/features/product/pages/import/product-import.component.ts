import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { Papa, ParseConfig, ParseResult } from 'ngx-papaparse';
import { IProduct, RawProduct } from '../../models/product.model';
import { FileUploader } from 'ng2-file-upload';

@Component({
  selector: 'stl-product-import',
  templateUrl: './product-import.component.html',
  styleUrls: ['./product-import.component.scss'],
})
export class ProductImportComponent {
  public contentHeader: ContentHeader;
  public hasBaseDropZoneOver = false;
  public uploader: FileUploader = new FileUploader({
    isHTML5: true,
  });
  public fileItem: File | undefined = undefined;
  public products: IProduct[] = [];
  public config: any;
  public error = false;
  public loading = false;
  public ACCEPTED_FILES = ['text/csv, text/tsv'];
  pageAdvancedEllipses = 7;

  constructor(private papa: Papa) {
    this.contentHeader = {
      headerTitle: 'Product importer',
      actionButton: true,
      breadcrumb: {
        type: '',
        links: [
          {
            name: 'Home',
            isLink: true,
            link: '/',
          },
          {
            name: 'Products',
            isLink: true,
            link: '/products',
          },
          {
            name: 'Import',
            isLink: false,
          },
        ],
      },
    };
    this.config = {
      itemsPerPage: 5,
      currentPage: 1,
      totalItems: this.products.length,
    };
  }

  fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  pageChanged(event: any): void {
    this.config.currentPage = event;
  }

  /**
   * on file drop handler
   */
  onFileDropped($event: any): void {
    this.loading = true;
    this.error = false;
    const droopedFile = $event[0];
    if (droopedFile.type !== 'text/csv') {
      this.error = true;
      return;
    }
    const options: ParseConfig = {
      worker: true,
      complete: (results: ParseResult, file?: File) => {
        this.loading = false;
        this.fileItem = file;
        this.products = results.data.map((rawProduct: any) => new RawProduct(rawProduct[1], rawProduct[2], rawProduct[10]));
        this.products.shift();
      },
      error: () => {
        this.error = true;
        this.loading = false;
      },
    };
    this.papa.parse(droopedFile, options);
  }

  removeFile(): void {
    this.fileItem = undefined;
  }
}
