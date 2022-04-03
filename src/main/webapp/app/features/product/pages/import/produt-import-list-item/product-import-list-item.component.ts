import { Component, EventEmitter, Input, Output } from '@angular/core';
import { ProductImport } from '../../../models/product.model';

@Component({
  selector: 'stl-product-import-list-item,tr[stl-product-import-list-item]',
  templateUrl: './product-import-list-item.component.html',
  styleUrls: ['./product-import-list-item.component.scss'],
})
export class ProductImportListItemComponent {
  @Input()
  public product?: ProductImport;

  @Input()
  public loading?: boolean = false;

  @Output()
  public remove: EventEmitter<ProductImport> = new EventEmitter();
}
