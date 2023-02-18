import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportModalErrorComponent } from './import-modal-error.component';

describe('ImportModalErrorComponent', () => {
  let component: ImportModalErrorComponent;
  let fixture: ComponentFixture<ImportModalErrorComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ImportModalErrorComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportModalErrorComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
