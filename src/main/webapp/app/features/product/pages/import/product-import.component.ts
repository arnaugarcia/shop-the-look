import { Component } from '@angular/core';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { Papa } from 'ngx-papaparse';
import { IProduct, Product } from '../../models/product.model';

@Component({
  selector: 'stl-product-import',
  templateUrl: './product-import.component.html',
  styleUrls: ['./product-import.component.scss'],
})
export class ProductImportComponent {
  public contentHeader: ContentHeader;
  public products: IProduct[] = [];
  private error = false;

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
  }

  /**
   * on file drop handler
   */
  onFileDropped($event: any): void {
    this.error = false;
    const file = $event[0];
    if (file.type !== 'text/csv') {
      this.error = true;
      return;
    }
    const options: any = {
      worker: true,
      complete: (results: any) => {
        this.products = results.data.map((rawProduct: any) => new Product(0, 'SKU', rawProduct[2]));
      },
    };
    this.papa.parse(file, options);
    // this.prepareFilesList($event);
  }

  /**
   * handle file from browsing
   */
  fileBrowseHandler(files: any): void {
    //this.prepareFilesList(files);
  }

  /**
   * Delete file from files list
   * @param index (File index)
   */
  deleteFile(index: number): void {
    // this.files.splice(index, 1);
  }
}
