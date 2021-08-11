import { Injectable } from '@angular/core';
import { ImportProduct } from '../models/product-import.model';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { IProduct } from '../models/product.model';
import { EntityResponseType } from './product.service';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class ProductImportService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products/import');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  import(importProduct: ImportProduct): Observable<EntityResponseType> {
    const options = createRequestOption({
      companyReference: importProduct.companyReference,
    });
    if (importProduct.update) {
      return this.http.put<IProduct>(this.resourceUrl, importProduct.products, {
        observe: 'response',
        params: options,
      });
    }
    return this.http.post<IProduct>(this.resourceUrl, importProduct.products, { observe: 'response', params: options });
  }
}
