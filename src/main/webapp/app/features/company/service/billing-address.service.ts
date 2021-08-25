import { Injectable } from '@angular/core';
import { HttpClient, HttpParams, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { IBillingAddress } from '../model/billing-address.model';

export type EntityResponseType = HttpResponse<IBillingAddress>;
export type EntityArrayResponseType = HttpResponse<IBillingAddress[]>;

@Injectable({ providedIn: 'root' })
export class BillingAddressService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/companies/billing');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  update(billingAddress: IBillingAddress, companyReference?: string): Observable<EntityResponseType> {
    const options = this.buildRequestParams(companyReference);
    return this.http.put<IBillingAddress>(`${this.resourceUrl}`, billingAddress, {
      params: options,
      observe: 'response',
    });
  }

  query(companyReference?: string): Observable<EntityResponseType> {
    const options = this.buildRequestParams(companyReference);
    return this.http.get<IBillingAddress>(this.resourceUrl, { params: options, observe: 'response' });
  }

  private buildRequestParams(companyReference: string | undefined): HttpParams {
    let options: HttpParams = new HttpParams();
    if (companyReference) {
      options = options.append('companyReference', companyReference);
    }
    return options;
  }
}
