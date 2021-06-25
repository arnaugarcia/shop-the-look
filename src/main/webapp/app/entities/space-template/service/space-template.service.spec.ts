import { TestBed } from '@angular/core/testing';
import { HttpClientTestingModule, HttpTestingController } from '@angular/common/http/testing';

import { ISpaceTemplate, SpaceTemplate } from '../space-template.model';

import { SpaceTemplateService } from './space-template.service';

describe('Service Tests', () => {
  describe('SpaceTemplate Service', () => {
    let service: SpaceTemplateService;
    let httpMock: HttpTestingController;
    let elemDefault: ISpaceTemplate;
    let expectedResult: ISpaceTemplate | ISpaceTemplate[] | boolean | null;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
      });
      expectedResult = null;
      service = TestBed.inject(SpaceTemplateService);
      httpMock = TestBed.inject(HttpTestingController);

      elemDefault = {
        id: 0,
        name: 'AAAAAAA',
        description: 'AAAAAAA',
        maxProducts: 0,
        maxPhotos: 0,
        active: false,
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

      it('should create a SpaceTemplate', () => {
        const returnedFromService = Object.assign(
          {
            id: 0,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.create(new SpaceTemplate()).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'POST' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should update a SpaceTemplate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            maxProducts: 1,
            maxPhotos: 1,
            active: true,
          },
          elemDefault
        );

        const expected = Object.assign({}, returnedFromService);

        service.update(expected).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PUT' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should partial update a SpaceTemplate', () => {
        const patchObject = Object.assign(
          {
            description: 'BBBBBB',
            active: true,
          },
          new SpaceTemplate()
        );

        const returnedFromService = Object.assign(patchObject, elemDefault);

        const expected = Object.assign({}, returnedFromService);

        service.partialUpdate(patchObject).subscribe(resp => (expectedResult = resp.body));

        const req = httpMock.expectOne({ method: 'PATCH' });
        req.flush(returnedFromService);
        expect(expectedResult).toMatchObject(expected);
      });

      it('should return a list of SpaceTemplate', () => {
        const returnedFromService = Object.assign(
          {
            id: 1,
            name: 'BBBBBB',
            description: 'BBBBBB',
            maxProducts: 1,
            maxPhotos: 1,
            active: true,
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

      it('should delete a SpaceTemplate', () => {
        service.delete(123).subscribe(resp => (expectedResult = resp.ok));

        const req = httpMock.expectOne({ method: 'DELETE' });
        req.flush({ status: 200 });
        expect(expectedResult);
      });

      describe('addSpaceTemplateToCollectionIfMissing', () => {
        it('should add a SpaceTemplate to an empty array', () => {
          const spaceTemplate: ISpaceTemplate = { id: 123 };
          expectedResult = service.addSpaceTemplateToCollectionIfMissing([], spaceTemplate);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(spaceTemplate);
        });

        it('should not add a SpaceTemplate to an array that contains it', () => {
          const spaceTemplate: ISpaceTemplate = { id: 123 };
          const spaceTemplateCollection: ISpaceTemplate[] = [
            {
              ...spaceTemplate,
            },
            { id: 456 },
          ];
          expectedResult = service.addSpaceTemplateToCollectionIfMissing(spaceTemplateCollection, spaceTemplate);
          expect(expectedResult).toHaveLength(2);
        });

        it("should add a SpaceTemplate to an array that doesn't contain it", () => {
          const spaceTemplate: ISpaceTemplate = { id: 123 };
          const spaceTemplateCollection: ISpaceTemplate[] = [{ id: 456 }];
          expectedResult = service.addSpaceTemplateToCollectionIfMissing(spaceTemplateCollection, spaceTemplate);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(spaceTemplate);
        });

        it('should add only unique SpaceTemplate to an array', () => {
          const spaceTemplateArray: ISpaceTemplate[] = [{ id: 123 }, { id: 456 }, { id: 1565 }];
          const spaceTemplateCollection: ISpaceTemplate[] = [{ id: 123 }];
          expectedResult = service.addSpaceTemplateToCollectionIfMissing(spaceTemplateCollection, ...spaceTemplateArray);
          expect(expectedResult).toHaveLength(3);
        });

        it('should accept varargs', () => {
          const spaceTemplate: ISpaceTemplate = { id: 123 };
          const spaceTemplate2: ISpaceTemplate = { id: 456 };
          expectedResult = service.addSpaceTemplateToCollectionIfMissing([], spaceTemplate, spaceTemplate2);
          expect(expectedResult).toHaveLength(2);
          expect(expectedResult).toContain(spaceTemplate);
          expect(expectedResult).toContain(spaceTemplate2);
        });

        it('should accept null and undefined values', () => {
          const spaceTemplate: ISpaceTemplate = { id: 123 };
          expectedResult = service.addSpaceTemplateToCollectionIfMissing([], null, spaceTemplate, undefined);
          expect(expectedResult).toHaveLength(1);
          expect(expectedResult).toContain(spaceTemplate);
        });

        it('should return initial array if no SpaceTemplate is added', () => {
          const spaceTemplateCollection: ISpaceTemplate[] = [{ id: 123 }];
          expectedResult = service.addSpaceTemplateToCollectionIfMissing(spaceTemplateCollection, undefined, null);
          expect(expectedResult).toEqual(spaceTemplateCollection);
        });
      });
    });

    afterEach(() => {
      httpMock.verify();
    });
  });
});
