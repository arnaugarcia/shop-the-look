import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IProduct } from '../models/product.model';

export type EntityResponseType = HttpResponse<IProduct>;
export type EntityArrayResponseType = HttpResponse<IProduct[]>;

@Injectable({ providedIn: 'root' })
export class ProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(products: IProduct[]): Observable<EntityResponseType> {
    return this.http.post<IProduct>(this.resourceUrl, products, { observe: 'response' });
  }

  update(product: IProduct): Observable<EntityResponseType> {
    return this.http.put<IProduct>(`${this.resourceUrl}/${product.reference!}`, product, { observe: 'response' });
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IProduct[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(reference: string): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }
}
