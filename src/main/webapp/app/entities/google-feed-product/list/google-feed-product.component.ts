import { Component, OnInit } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { NgbModal } from '@ng-bootstrap/ng-bootstrap';

import { IGoogleFeedProduct } from '../google-feed-product.model';
import { GoogleFeedProductService } from '../service/google-feed-product.service';
import { GoogleFeedProductDeleteDialogComponent } from '../delete/google-feed-product-delete-dialog.component';

@Component({
  selector: 'stl-google-feed-product',
  templateUrl: './google-feed-product.component.html',
})
export class GoogleFeedProductComponent implements OnInit {
  googleFeedProducts?: IGoogleFeedProduct[];
  isLoading = false;

  constructor(protected googleFeedProductService: GoogleFeedProductService, protected modalService: NgbModal) {}

  loadAll(): void {
    this.isLoading = true;

    this.googleFeedProductService.query().subscribe(
      (res: HttpResponse<IGoogleFeedProduct[]>) => {
        this.isLoading = false;
        this.googleFeedProducts = res.body ?? [];
      },
      () => {
        this.isLoading = false;
      }
    );
  }

  ngOnInit(): void {
    this.loadAll();
  }

  trackId(index: number, item: IGoogleFeedProduct): number {
    return item.id!;
  }

  delete(googleFeedProduct: IGoogleFeedProduct): void {
    const modalRef = this.modalService.open(GoogleFeedProductDeleteDialogComponent, { size: 'lg', backdrop: 'static' });
    modalRef.componentInstance.googleFeedProduct = googleFeedProduct;
    // unsubscribe not needed because closed completes on modal close
    modalRef.closed.subscribe(reason => {
      if (reason === 'deleted') {
        this.loadAll();
      }
    });
  }
}
