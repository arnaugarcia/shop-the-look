import { Component } from '@angular/core';
import { FileUploader } from 'ng2-file-upload';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';

const URL = 'https://your-url.com';

@Component({
  selector: 'stl-product-create',
  templateUrl: './product-create.component.html',
  styleUrls: ['./product-create.component.sass'],
})
export class ProductCreateComponent {
  public contentHeader: ContentHeader;
  public hasAnotherDropZoneOver = false;
  public hasBaseDropZoneOver = false;
  public uploader: FileUploader = new FileUploader({
    url: URL,
    isHTML5: true,
  });

  constructor() {
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
            link: 'products',
          },
          {
            name: 'Import',
            isLink: false,
          },
        ],
      },
    };
  }

  fileOverBase(e: any): void {
    this.hasBaseDropZoneOver = e;
  }

  fileOverAnother(e: any): void {
    this.hasAnotherDropZoneOver = e;
  }
}
