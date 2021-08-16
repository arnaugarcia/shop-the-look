import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';
import { Observable } from 'rxjs';
import { createRequestOption } from '../../../core/request/request-util';
import { IEmployee } from '../models/employee.model';

export type EntityResponseType = HttpResponse<IEmployee>;
export type EntityArrayResponseType = HttpResponse<IEmployee[]>;

@Injectable({
  providedIn: 'root',
})
export class EmployeeService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/employees');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  query(req?: any): Observable<EntityArrayResponseType> {
    const options = createRequestOption(req);
    return this.http.get<IEmployee[]>(this.resourceUrl, { params: options, observe: 'response' });
  }

  manager(employee: IEmployee): Observable<EntityResponseType> {
    return this.http.put<IEmployee>(`${this.resourceUrl}/${employee.login}/manager`, null, { observe: 'response' });
  }

  remove(login: string): Observable<EntityResponseType> {
    return this.http.delete<IEmployee>(`${this.resourceUrl}/${login}`, { observe: 'response' });
  }
}
