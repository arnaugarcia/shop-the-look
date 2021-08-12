import { Injectable } from '@angular/core';
import { ImportProduct } from '../models/product-import.model';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { IProduct } from '../models/product.model';
import { EntityArrayResponseType } from './product.service';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class ProductImportService {
  protected importResourceUrl = this.applicationConfigService.getEndpointFor('api/products/import');
  protected refreshResourceUrl = this.applicationConfigService.getEndpointFor('api/products/refresh');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  import(importProduct: ImportProduct): Observable<EntityArrayResponseType> {
    const options = createRequestOption({
      companyReference: importProduct.companyReference,
    });
    if (importProduct.update) {
      return this.http.put<IProduct[]>(this.importResourceUrl, importProduct.products, {
        observe: 'response',
        params: options,
      });
    }
    return this.http.post<IProduct[]>(this.importResourceUrl, importProduct.products, {
      observe: 'response',
      params: options,
    });
  }

  refresh(): Observable<EntityArrayResponseType> {
    return this.http.put<IProduct[]>(this.refreshResourceUrl, null, { observe: 'response' });
  }
}
