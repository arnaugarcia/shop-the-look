import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { GoogleFeedProductService } from '../service/google-feed-product.service';

import { GoogleFeedProductComponent } from './google-feed-product.component';

describe('Component Tests', () => {
  describe('GoogleFeedProduct Management Component', () => {
    let comp: GoogleFeedProductComponent;
    let fixture: ComponentFixture<GoogleFeedProductComponent>;
    let service: GoogleFeedProductService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [GoogleFeedProductComponent],
      })
        .overrideTemplate(GoogleFeedProductComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(GoogleFeedProductComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(GoogleFeedProductService);

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
      expect(comp.googleFeedProducts?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
