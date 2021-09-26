import { Injectable } from '@angular/core';
import { Resolve } from '@angular/router';
import { Observable, of } from 'rxjs';
import { ISpace } from '../model/space.model';
import { SpaceService } from '../service/space.service';
import { HttpResponse } from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SpacesRoutingResolver implements Resolve<ISpace[]> {
  constructor(private spaceService: SpaceService) {}
  resolve(): Observable<ISpace[]> {
    this.spaceService.queryForCurrentUser().subscribe((response: HttpResponse<ISpace[]>) => {
      if (response.ok && response.body) {
        return of(response.body);
      } else {
        return of([]);
      }
    });
    return of([]);
  }
}
