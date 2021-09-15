import { Component, OnInit } from '@angular/core';
import { IProduct } from '../../../product/models/product.model';
import { ProductService } from '../../../product/services/product.service';
import { HttpResponse } from '@angular/common/http';

@Component({
  selector: 'stl-product-search',
  templateUrl: './product-search.component.html',
  styleUrls: ['./product-search.component.scss'],
})
export class ProductSearchComponent implements OnInit {
  public products: IProduct[] = [];

  constructor(private productService: ProductService) {}

  ngOnInit(): void {
    this.productService.query().subscribe((response: HttpResponse<IProduct[]>) => {
      this.products = response.body!;
    });
  }
}
