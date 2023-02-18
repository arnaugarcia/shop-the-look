import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import { ApplicationConfigService } from 'app/core/config/application-config.service';
import { createRequestOption } from 'app/core/request/request-util';
import { ICompany } from '../model/company.model';

export type EntityResponseType = HttpResponse<ICompany>;
export type EntityArrayResponseType = HttpResponse<ICompany[]>;

@Injectable({ providedIn: 'root' })
export class CompanyService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/companies');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  create(company: ICompany): Observable<EntityResponseType> {
    return this.http.post<ICompany>(this.resourceUrl, company, { observe: 'response' });
  }

  update(company: ICompany): Observable<EntityResponseType> {
    return this.http.put<ICompany>(`${this.resourceUrl}`, company, { observe: 'response' });
  }

  find(reference: string): Observable<EntityResponseType> {
    return this.http.get<ICompany>(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }

  findByCurrentUser(): Observable<EntityResponseType> {
    return this.http.get<ICompany>(`${this.applicationConfigService.getEndpointFor('api/company')}`, { observe: 'response' });
  }

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<ICompany[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  delete(reference: string): Observable<HttpResponse<void>> {
    return this.http.delete<void>(`${this.resourceUrl}/${reference}`, { observe: 'response' });
  }
}
