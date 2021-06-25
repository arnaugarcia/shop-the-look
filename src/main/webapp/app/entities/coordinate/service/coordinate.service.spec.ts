import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ICoordinate, Coordinate } from '../coordinate.model';

import { CoordinateService } from './coordinate.service';

describe('Service Tests', () => {
  describe('Coordinate Service', () => {
    let service: CoordinateService;
    let httpMock: HttpTestingController;
    let elemDefault: ICoordinate;
    let expectedResult: ICoordinate | ICoordinate[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(CoordinateService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        x: 0,
        y: 0,
      };
    });

    describe('Service methods', () => {
      it('should find an element', () => {
        const returnedFromService = Object.assign({}, elemDefault);

        service.find(123).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(elemDefault);
      });

      it('should create a Coordinate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Coordinate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Coordinate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            x: 1,
            y: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Coordinate', () => {
        const patchObject = Object.assign({}, new Coordinate());

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Coordinate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            x: 1,
            y: 1,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.query().subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'GET' });
        req.flush([returnedFromService]);
        httpMock.verify();
        expect(expectedResult).toContainEqual(expected);
      });

      it('should delete a Coordinate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addCoordinateToCollectionIfMissing', () => {
        it('should add a Coordinate to an empty array', () => {
          const coordinate: ICoordinate = { id: 123 };
          expectedResult = service.addCoordinateToCollectionIfMissing([], coordinate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(coordinate);
        });

        it('should not add a Coordinate to an array that contains it', () => {
          const coordinate: ICoordinate = { id: 123 };
          const coordinateCollection: ICoordinate[] = [
            {
              ...coordinate,
            },
            { id: 456 },
          ];
          expectedResult = service.addCoordinateToCollectionIfMissing(coordinateCollection, coordinate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Coordinate to an array that doesn't contain it", () => {
          const coordinate: ICoordinate = { id: 123 };
          const coordinateCollection: ICoordinate[] = [{ id: 456 }];
          expectedResult = service.addCoordinateToCollectionIfMissing(coordinateCollection, coordinate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(coordinate);
        });

        it('should add only unique Coordinate to an array', () => {
          const coordinateArray: ICoordinate[] = [{ id: 123 }, { id: 456 }, { id: 55767 }];
          const coordinateCollection: ICoordinate[] = [{ id: 123 }];
          expectedResult = service.addCoordinateToCollectionIfMissing(coordinateCollection, ...coordinateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const coordinate: ICoordinate = { id: 123 };
          const coordinate2: ICoordinate = { id: 456 };
          expectedResult = service.addCoordinateToCollectionIfMissing([], coordinate, coordinate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(coordinate);
          expect(expectedResult).toContain(coordinate2);
        });

        it('should accept null and undefined values', () => {
          const coordinate: ICoordinate = { id: 123 };
          expectedResult = service.addCoordinateToCollectionIfMissing([], null, coordinate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(coordinate);
        });

        it('should return initial array if no Coordinate is added', () => {
          const coordinateCollection: ICoordinate[] = [{ id: 123 }];
          expectedResult = service.addCoordinateToCollectionIfMissing(coordinateCollection, undefined, null);
          expect(expectedResult).toEqual(coordinateCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
