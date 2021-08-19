import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IBillingAddress } from '../model/billing-address.model';

export type EntityResponseType = HttpResponse<IBillingAddress>;
export type EntityArrayResponseType = HttpResponse<IBillingAddress[]>;

@Injectable({ providedIn: 'root' })
export class BillingAddressService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/billing-addresses');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(billingAddress: IBillingAddress): Observable<EntityResponseType> {
    return this.http.put<IBillingAddress>(`${this.resourceUrl}/asdff`, billingAddress, {
      observe: 'response',
    });
  }

  query(): Observable<EntityArrayResponseType> {
    return this.http.get<IBillingAddress[]>(this.resourceUrl, { observe: 'response' });
  }
}
