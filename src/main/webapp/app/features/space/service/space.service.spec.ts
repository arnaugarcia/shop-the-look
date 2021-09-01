import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpace, Space } from '../space.model';

import { SpaceService } from './space.service';

describe('Service Tests', () => {
  describe('Space Service', () => {
    let service: SpaceService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpace;
    let expectedResult: ISpace | ISpace[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SpaceService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        active: false,
        reference: 'AAAAAAA',
        description: 'AAAAAAA',
        maxPhotos: 0,
        visible: false,
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

      it('should create a Space', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new Space()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a Space', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            active: true,
            reference: 'BBBBBB',
            description: 'BBBBBB',
            maxPhotos: 1,
            visible: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a Space', () => {
        const patchObject = Object.assign(
          {
            active: true,
            description: 'BBBBBB',
            visible: true,
          },
          new Space()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of Space', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            active: true,
            reference: 'BBBBBB',
            description: 'BBBBBB',
            maxPhotos: 1,
            visible: true,
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

      it('should delete a Space', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSpaceToCollectionIfMissing', () => {
        it('should add a Space to an empty array', () => {
          const space: ISpace = { id: 123 };
          expectedResult = service.addSpaceToCollectionIfMissing([], space);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(space);
        });

        it('should not add a Space to an array that contains it', () => {
          const space: ISpace = { id: 123 };
          const spaceCollection: ISpace[] = [
            {
              ...space,
            },
            { id: 456 },
          ];
          expectedResult = service.addSpaceToCollectionIfMissing(spaceCollection, space);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a Space to an array that doesn't contain it", () => {
          const space: ISpace = { id: 123 };
          const spaceCollection: ISpace[] = [{ id: 456 }];
          expectedResult = service.addSpaceToCollectionIfMissing(spaceCollection, space);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(space);
        });

        it('should add only unique Space to an array', () => {
          const spaceArray: ISpace[] = [{ id: 123 }, { id: 456 }, { id: 73131 }];
          const spaceCollection: ISpace[] = [{ id: 123 }];
          expectedResult = service.addSpaceToCollectionIfMissing(spaceCollection, ...spaceArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const space: ISpace = { id: 123 };
          const space2: ISpace = { id: 456 };
          expectedResult = service.addSpaceToCollectionIfMissing([], space, space2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(space);
          expect(expectedResult).toContain(space2);
        });

        it('should accept null and undefined values', () => {
          const space: ISpace = { id: 123 };
          expectedResult = service.addSpaceToCollectionIfMissing([], null, space, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(space);
        });

        it('should return initial array if no Space is added', () => {
          const spaceCollection: ISpace[] = [{ id: 123 }];
          expectedResult = service.addSpaceToCollectionIfMissing(spaceCollection, undefined, null);
          expect(expectedResult).toEqual(spaceCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
