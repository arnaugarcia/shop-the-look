import { Component, ElementRef, ViewChild } from '@angular/core';
import { IProduct } from '../../../product/models/product.model';
import { ProductService } from '../../../product/services/product.service';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.scss'],
})
export class ProductSearchComponent {
  public products: IProduct[] = [];

  @ViewChild('openSearch') private _inputElement?: ElementRef;

  constructor(public activeModal: NgbActiveModal, private productService: ProductService) {}
  public selectProduct(product: IProduct): void {
    this.activeModal.close({ productReference: product.reference });
  }

  public searchUpdate($event: any): void {
    const val = $event.target.value.toLowerCase();
    setTimeout(() => {
      this.productService.query({ keyword: val }).subscribe((response: HttpResponse<IProduct[]>) => {
        this.products = response.body!;
      });
    }, 250);
  }
}
