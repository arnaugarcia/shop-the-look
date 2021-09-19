import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { ApplicationConfigService } from '../../../core/config/application-config.service';

@Injectable({
  providedIn: 'root',
})
export class SpaceCoordinateService {
  constructor(protected http: HttpClient, protected applicationConfigService: ApplicationConfigService) {}

  addCoordinate(param: { productReference: string; x: any; photoReference: string; y: any }): void {
    console.error(param);
  }

  removeCoordinate(spaceReference: string, photoReference: string, coordinateReference: string): void {
    console.error(spaceReference, photoReference, coordinateReference);
  }
}
