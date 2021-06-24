import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { CoordinateService } from '../service/coordinate.service';

import { CoordinateComponent } from './coordinate.component';

describe('Component Tests', () => {
  describe('Coordinate Management Component', () => {
    let comp: CoordinateComponent;
    let fixture: ComponentFixture<CoordinateComponent>;
    let service: CoordinateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [CoordinateComponent],
      })
        .overrideTemplate(CoordinateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(CoordinateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(CoordinateService);

      const headers = new HttpHeaders().append('link', 'link;link');
      jest.spyOn(service, 'query').mockReturnValue(
        of(
          new HttpResponse({
            body: [{ id: 123 }],
            headers,
          })
        )
      );
    });

    it('Should call load all on init', () => {
      // WHEN
      comp.ngOnInit();

      // THEN
      expect(service.query).toHaveBeenCalled();
      expect(comp.coordinates?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
