import { Injectable } from '@angular/core';
import { IPhoto, PhotoRequest } from '../model/photo.model';
import { Observable } from 'rxjs';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

export type EntityResponseType = HttpResponse<IPhoto>;

@Injectable({
  providedIn: 'root',
})
export class SpacePhotoService {
  protected resourceUrl = this.applicationConfigService.getEndpointFor('api/spaces');

  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  addPhoto(spaceReference: string, photoRequest: PhotoRequest): Observable<EntityResponseType> {
    return this.http.post<IPhoto>(`${this.resourceUrl}/${spaceReference}/photos`, photoRequest, { observe: 'response' });
  }

  removePhoto(spaceReference: string, photoReference: string): Observable<HttpResponse<void>> {
    return this.http.delete<void>(`${this.resourceUrl}/${spaceReference}/photos/${photoReference}`, { observe: 'response' });
  }
}
