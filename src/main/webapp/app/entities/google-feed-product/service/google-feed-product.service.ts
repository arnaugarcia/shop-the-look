import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { map } from 'rxjs/operators';
import * as dayjs from 'dayjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IGoogleFeedProduct, getGoogleFeedProductIdentifier } from '../google-feed-product.model';

export type EntityResponseType = HttpResponse<IGoogleFeedProduct>;
export type EntityArrayResponseType = HttpResponse<IGoogleFeedProduct[]>;

@Injectable({ providedIn: 'root' })
export class GoogleFeedProductService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/google-feed-products');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(googleFeedProduct: IGoogleFeedProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleFeedProduct);
    return this.http
      .post<IGoogleFeedProduct>(this.resourceUrl, copy, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  update(googleFeedProduct: IGoogleFeedProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleFeedProduct);
    return this.http
      .put<IGoogleFeedProduct>(`${this.resourceUrl}/${getGoogleFeedProductIdentifier(googleFeedProduct) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  partialUpdate(googleFeedProduct: IGoogleFeedProduct): Observable<EntityResponseType> {
    const copy = this.convertDateFromClient(googleFeedProduct);
    return this.http
      .patch<IGoogleFeedProduct>(`${this.resourceUrl}/${getGoogleFeedProductIdentifier(googleFeedProduct) as number}`, copy, {
        observe: 'response',
      })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http
      .get<IGoogleFeedProduct>(`${this.resourceUrl}/${id}`, { observe: 'response' })
      .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http
      .get<IGoogleFeedProduct[]>(this.resourceUrl, { params: options, observe: 'response' })
      .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addGoogleFeedProductToCollectionIfMissing(
    googleFeedProductCollection: IGoogleFeedProduct[],
    ...googleFeedProductsToCheck: (IGoogleFeedProduct | null | undefined)[]
  ): IGoogleFeedProduct[] {
    const googleFeedProducts: IGoogleFeedProduct[] = googleFeedProductsToCheck.filter(isPresent);
    if (googleFeedProducts.length > 0) {
      const googleFeedProductCollectionIdentifiers = googleFeedProductCollection.map(
        googleFeedProductItem => getGoogleFeedProductIdentifier(googleFeedProductItem)!
      );
      const googleFeedProductsToAdd = googleFeedProducts.filter(googleFeedProductItem => {
        const googleFeedProductIdentifier = getGoogleFeedProductIdentifier(googleFeedProductItem);
        if (googleFeedProductIdentifier == null || googleFeedProductCollectionIdentifiers.includes(googleFeedProductIdentifier)) {
          return false;
        }
        googleFeedProductCollectionIdentifiers.push(googleFeedProductIdentifier);
        return true;
      });
      return [...googleFeedProductsToAdd, ...googleFeedProductCollection];
    }
    return googleFeedProductCollection;
  }

  protected convertDateFromClient(googleFeedProduct: IGoogleFeedProduct): IGoogleFeedProduct {
    return Object.assign({}, googleFeedProduct, {
      availabilityDate: googleFeedProduct.availabilityDate?.isValid() ? googleFeedProduct.availabilityDate.toJSON() : undefined,
    });
  }

  protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
    if (res.body) {
      res.body.availabilityDate = res.body.availabilityDate ? dayjs(res.body.availabilityDate) : undefined;
    }
    return res;
  }

  protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
    if (res.body) {
      res.body.forEach((googleFeedProduct: IGoogleFeedProduct) => {
        googleFeedProduct.availabilityDate = googleFeedProduct.availabilityDate ? dayjs(googleFeedProduct.availabilityDate) : undefined;
      });
    }
    return res;
  }
}
