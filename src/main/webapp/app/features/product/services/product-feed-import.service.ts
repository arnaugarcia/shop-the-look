import { Injectable } from '@angular/core';
import { Observable } from 'rxjs';
import { IProduct } from '../models/product.model';
import { EntityArrayResponseType } from './product.service';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class ProductFeedImportService {
  protected refreshResourceUrl = this.applicationConfigService.getEndpointFor('api/products/feed');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  refreshFeed(): Observable<EntityArrayResponseType> {
    return this.http.put<IProduct[]>(this.refreshResourceUrl, null, { observe: 'response' });
  }
}
