import { Component, OnDestroy, OnInit } from '@angular/core';
import { IProduct } from '../../models/product.model';
import { ProductService } from '../../services/product.service';
import { ContentHeader } from '../../../../layouts/content-header/content-header.component';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { ASC, DESC, ITEMS_PER_PAGE, SORT } from '../../../../config/pagination.constants';
import { ActivatedRoute, Router } from '@angular/router';
import { combineLatest, Subject, Subscription } from 'rxjs';
import { debounceTime, distinctUntilChanged } from 'rxjs/operators';
import { ProductFeedImportService } from '../../services/product-feed-import.service';
import { ImportMethod, IPreferences } from '../../../company/model/preferences.model';
import { PreferencesService } from '../../../company/service/preferences.service';

@Component({
  selector: 'stl-product',
  templateUrl: './product.component.html',
})
export class ProductComponent implements OnInit, OnDestroy {
  public products: IProduct[] = [];
  public totalItems = 0;
  public itemsPerPage = ITEMS_PER_PAGE;
  public page?: number;
  public contentHeader: ContentHeader;
  public ngbPaginationPage = 1;

  public sizeChanged: Subject<number> = new Subject<number>();

  public isLoading = false;

  public preferredImportMethod?: ImportMethod;
  public remainingImports = 0;

  public searchText = '';
  public searchTextChanged: Subject<string> = new Subject<string>();
  private searchTextChangeSubscription: Subscription = new Subscription();
  private sizeChangeSubscription: Subscription = new Subscription();

  private predicate!: string;
  private ascending!: boolean;

  constructor(
    private router: Router,
    private feedService: ProductFeedImportService,
    private preferencesService: PreferencesService,
    protected activatedRoute: ActivatedRoute,
    protected productService: ProductService
  ) {
    this.contentHeader = {
      headerTitle: 'Products',
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
            isLink: false,
            link: '/products',
          },
        ],
      },
    };
  }

  ngOnInit(): void {
    this.handleNavigation();
    this.searchTextChangeSubscription = this.searchTextChanged
      .pipe(debounceTime(500), distinctUntilChanged())
      .subscribe((newText: string) => {
        this.searchText = newText;
        this.loadPage();
      });
    this.sizeChangeSubscription = this.sizeChanged.subscribe((size: number) => {
      this.itemsPerPage = size;
      this.loadPage();
    });
    this.loadPreferences();
  }

  ngOnDestroy(): void {
    this.searchTextChangeSubscription.unsubscribe();
  }

  trackReference(index: number, item: IProduct): string {
    return item.reference!;
  }

  loadAll(): void {
    this.isLoading = true;

    this.productService
      .query({
        size: this.itemsPerPage,
        sort: this.sort(),
        keyword: this.searchText,
      })
      .subscribe(
        (response: HttpResponse<IProduct[]>) => {
          this.onSuccess(response.body, response.headers, 1, true);
          this.isLoading = false;
        },
        () => {
          this.isLoading = false;
        }
      );
  }

  refreshProducts(): void {
    if (!this.canImportWithFeed()) {
      return;
    }
    this.isLoading = true;
    this.feedService.refreshFeed().subscribe(
      (res: HttpResponse<IProduct[]>) => {
        this.isLoading = false;
        this.loadPreferences();
        this.onSuccess(res.body, res.headers, 1, true);
      },
      () => {
        this.isLoading = false;
        this.onError();
      }
    );
  }

  canImportWithFeed(): boolean {
    return (
      !this.isLoading && this.preferredImportMethod !== undefined && this.preferredImportMethod === 'FEED' && this.remainingImports > 0
    );
  }

  loadPage(page?: number, dontNavigate?: boolean): void {
    this.isLoading = true;
    const pageToLoad: number = page ?? this.page ?? 1;

    this.productService
      .query({
        page: pageToLoad - 1,
        size: this.itemsPerPage,
        sort: this.sort(),
        keyword: this.searchText,
      })
      .subscribe(
        (res: HttpResponse<IProduct[]>) => {
          this.isLoading = false;
          this.onSuccess(res.body, res.headers, pageToLoad, !dontNavigate);
        },
        () => {
          this.isLoading = false;
          this.onError();
        }
      );
  }

  protected handleNavigation(): void {
    combineLatest([this.activatedRoute.data, this.activatedRoute.queryParamMap]).subscribe(([data, params]) => {
      const page = params.get('page');
      const pageNumber = page !== null ? +page : 1;
      const sort = (params.get(SORT) ?? data['defaultSort']).split(',');
      const predicate = sort[0];
      const ascending = sort[1] === ASC;
      if (pageNumber !== this.page || predicate !== this.predicate || ascending !== this.ascending) {
        this.predicate = predicate;
        this.ascending = ascending;
        this.loadPage(pageNumber, true);
      }
    });
  }

  protected onError(): void {
    this.ngbPaginationPage = this.page ?? 1;
  }

  protected sort(): string[] {
    const result = [this.predicate + ',' + (this.ascending ? ASC : DESC)];
    if (this.predicate !== 'sku') {
      result.push('sku');
    }
    return result;
  }

  protected onSuccess(data: IProduct[] | null, headers: HttpHeaders, page: number, navigate: boolean): void {
    this.totalItems = Number(headers.get('X-Total-Count'));
    this.page = page;
    if (navigate) {
      this.router.navigate(['/products'], {
        queryParams: {
          page: this.page,
          size: this.itemsPerPage,
          sort: this.predicate + ',' + (this.ascending ? ASC : DESC),
        },
      });
    }
    this.products = data ?? [];
    this.ngbPaginationPage = this.page;
  }

  private loadPreferences(): void {
    this.preferencesService.query().subscribe((response: HttpResponse<IPreferences>) => {
      const preference = response.body;
      if (!preference) {
        return;
      }
      this.preferredImportMethod = preference.importMethod;
      this.remainingImports = preference.remainingImports!;
    });
  }
}
