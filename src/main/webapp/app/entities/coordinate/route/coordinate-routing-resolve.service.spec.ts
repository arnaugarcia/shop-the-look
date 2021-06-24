jest.mock('@angular/router');

import { TestBed } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { ActivatedRouteSnapshot, Router } from '@angular/router';
import { of } from 'rxjs';

import { ICoordinate, Coordinate } from '../coordinate.model';
import { CoordinateService } from '../service/coordinate.service';

import { CoordinateRoutingResolveService } from './coordinate-routing-resolve.service';

describe('Service Tests', () => {
  describe('Coordinate routing resolve service', () => {
    let mockRouter: Router;
    let mockActivatedRouteSnapshot: ActivatedRouteSnapshot;
    let routingResolveService: CoordinateRoutingResolveService;
    let service: CoordinateService;
    let resultCoordinate: ICoordinate | undefined;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        providers: [Router, ActivatedRouteSnapshot],
      });
      mockRouter = TestBed.inject(Router);
      mockActivatedRouteSnapshot = TestBed.inject(ActivatedRouteSnapshot);
      routingResolveService = TestBed.inject(CoordinateRoutingResolveService);
      service = TestBed.inject(CoordinateService);
      resultCoordinate = undefined;
    });

    describe('resolve', () => {
      it('should return ICoordinate returned by find', () => {
        // GIVEN
        service.find = jest.fn(id => of(new HttpResponse({ body: { id } })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoordinate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoordinate).toEqual({ id: 123 });
      });

      it('should return new ICoordinate if id is not provided', () => {
        // GIVEN
        service.find = jest.fn();
        mockActivatedRouteSnapshot.params = {};

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoordinate = result;
        });

        // THEN
        expect(service.find).not.toBeCalled();
        expect(resultCoordinate).toEqual(new Coordinate());
      });

      it('should route to 404 page if data not found in server', () => {
        // GIVEN
        jest.spyOn(service, 'find').mockReturnValue(of(new HttpResponse({ body: null as unknown as Coordinate })));
        mockActivatedRouteSnapshot.params = { id: 123 };

        // WHEN
        routingResolveService.resolve(mockActivatedRouteSnapshot).subscribe(result => {
          resultCoordinate = result;
        });

        // THEN
        expect(service.find).toBeCalledWith(123);
        expect(resultCoordinate).toEqual(undefined);
        expect(mockRouter.navigate).toHaveBeenCalledWith(['404']);
      });
    });
  });
});
