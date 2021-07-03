import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';

import { isPresent } from 'app/core/util/operators';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { IBillingAddress, getBillingAddressIdentifier } from '../billing-address.model';

export type EntityResponseType = HttpResponse<IBillingAddress>;
export type EntityArrayResponseType = HttpResponse<IBillingAddress[]>;

@Injectable({ providedIn: 'root' })
export class BillingAddressService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/billing-addresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(billingAddress: IBillingAddress): Observable<EntityResponseType> {
    return this.http.post<IBillingAddress>(this.resourceUrl, billingAddress, { observe: 'response' });
  }

  update(billingAddress: IBillingAddress): Observable<EntityResponseType> {
    return this.http.put<IBillingAddress>(`${this.resourceUrl}/${getBillingAddressIdentifier(billingAddress) as number}`, billingAddress, {
      observe: 'response',
    });
  }

  partialUpdate(billingAddress: IBillingAddress): Observable<EntityResponseType> {
    return this.http.patch<IBillingAddress>(
      `${this.resourceUrl}/${getBillingAddressIdentifier(billingAddress) as number}`,
      billingAddress,
      { observe: 'response' }
    );
  }

  find(id: number): Observable<EntityResponseType> {
    return this.http.get<IBillingAddress>(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IBillingAddress[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(id: number): Observable<HttpResponse<{}>> {
    return this.http.delete(`${this.resourceUrl}/${id}`, { observe: 'response' });
  }

  addBillingAddressToCollectionIfMissing(
    billingAddressCollection: IBillingAddress[],
    ...billingAddressesToCheck: (IBillingAddress | null | undefined)[]
  ): IBillingAddress[] {
    const billingAddresses: IBillingAddress[] = billingAddressesToCheck.filter(isPresent);
    if (billingAddresses.length > 0) {
      const billingAddressCollectionIdentifiers = billingAddressCollection.map(
        billingAddressItem => getBillingAddressIdentifier(billingAddressItem)!
      );
      const billingAddressesToAdd = billingAddresses.filter(billingAddressItem => {
        const billingAddressIdentifier = getBillingAddressIdentifier(billingAddressItem);
        if (billingAddressIdentifier == null || billingAddressCollectionIdentifiers.includes(billingAddressIdentifier)) {
          return false;
        }
        billingAddressCollectionIdentifiers.push(billingAddressIdentifier);
        return true;
      });
      return [...billingAddressesToAdd, ...billingAddressCollection];
    }
    return billingAddressCollection;
  }
}
