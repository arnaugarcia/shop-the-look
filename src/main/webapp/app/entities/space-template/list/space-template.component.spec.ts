import { ComponentFixture, TestBed } from '@angular/core/testing';
import { HttpHeaders, HttpResponse } from '@angular/common/http';
import { HttpClientTestingModule } from '@angular/common/http/testing';
import { of } from 'rxjs';

import { SpaceTemplateService } from '../service/space-template.service';

import { SpaceTemplateComponent } from './space-template.component';

describe('Component Tests', () => {
  describe('SpaceTemplate Management Component', () => {
    let comp: SpaceTemplateComponent;
    let fixture: ComponentFixture<SpaceTemplateComponent>;
    let service: SpaceTemplateService;

    beforeEach(() => {
      TestBed.configureTestingModule({
        imports: [HttpClientTestingModule],
        declarations: [SpaceTemplateComponent],
      })
        .overrideTemplate(SpaceTemplateComponent, '')
        .compileComponents();

      fixture = TestBed.createComponent(SpaceTemplateComponent);
      comp = fixture.componentInstance;
      service = TestBed.inject(SpaceTemplateService);

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
      expect(comp.spaceTemplates?.[0]).toEqual(expect.objectContaining({ id: 123 }));
    });
  });
});
