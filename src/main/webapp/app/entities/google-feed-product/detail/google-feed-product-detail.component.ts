import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IGoogleFeedProduct } from '../google-feed-product.model';

@Component({
  selector: 'stl-google-feed-product-detail',
  templateUrl: './google-feed-product-detail.component.html',
})
export class GoogleFeedProductDetailComponent implements OnInit {
  googleFeedProduct: IGoogleFeedProduct | null = null;

  constructor(protected activatedRoute: ActivatedRoute) {}

  ngOnInit(): void {
    this.activatedRoute.data.subscribe(({ googleFeedProduct }) => {
      this.googleFeedProduct = googleFeedProduct;
    });
  }

  previousState(): void {
    window.history.back();
  }
}
