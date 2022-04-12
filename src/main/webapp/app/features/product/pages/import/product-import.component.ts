import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { Papa, ParseConfig, ParseResult } from 'ngx-papaparse';
import { ProductImport } from '../../models/product.model';
import { FileUploader } from 'ng2-file-upload';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';
import { ImportModalSuccessComponent } from '../../components/import-modal-success/import-modal-success.component';
import { ImportModalErrorComponent } from '../../components/import-modal-error/import-modal-error.component';
import { ImportProduct } from '../../models/product-import.model';
import { AccountService } from '../../../../core/auth/account.service';
import { ProductImportService } from '../../services/product-import.service';
import { HttpErrorResponse } from '@angular/common/http';

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
  public products: ProductImport[] = [];
  public config: any;
  public error = false;
  public loading = false;
  public ACCEPTED_FILES = ['text/csv, text/tsv'];
  public page = 1;
  public progressBar = 0;

  constructor(
    private papa: Papa,
    private accountService: AccountService,
    private productImportService: ProductImportService,
    private modalService: NgbModal
  ) {
    this.contentHeader = {
      headerTitle: 'Product importer',
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
    this.progressBar = 0;
    const droopedFile = $event[0];
    if (droopedFile.type !== 'text/csv') {
      this.error = true;
      return;
    }
    const options: ParseConfig = {
      header: true,
      skipEmptyLines: 'greedy',
      transformHeader: (header: string) => header.toUpperCase(),
      complete: (results: ParseResult) => {
        if (!results.data.length) {
          this.error = true;
        }
        this.products = results.data
          .map((rawProduct: any) => new ProductImport(rawProduct.SKU, rawProduct.NAME, rawProduct.URL, rawProduct.PRICE))
          .sort((product1: ProductImport) => (product1.isValid() ? 1 : -1));
        this.loading = false;
      },
      error: () => {
        this.error = true;
        this.loading = false;
      },
    };
    this.papa.parse(droopedFile, options);
  }

  importProducts(): void {
    this.loading = true;
    this.productImportService.import(new ImportProduct(this.products)).subscribe(
      () => {
        this.loading = false;
        this.progressBar = 100;
        this.modalService.open(ImportModalSuccessComponent, {
          centered: true,
          windowClass: 'modal modal-success',
        });
      },
      (error: HttpErrorResponse) => {
        this.loading = false;
        this.progressBar = 0;
        this.showErrorModal();
        this.removeAllFromQueue();
      }
    );
  }

  removeAllFromQueue(): void {
    this.products = [];
    this.progressBar = 0;
  }

  removeProduct(product: ProductImport): void {
    const index = this.products.indexOf(product);
    if (index > -1) {
      this.products.splice(index, 1);
    }
  }

  private showErrorModal(): void {
    this.modalService.open(ImportModalErrorComponent, {
      centered: true,
      windowClass: 'modal modal-danger',
    });
  }
}
