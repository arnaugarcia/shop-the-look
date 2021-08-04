import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ImportModalSuccessComponent } from './import-modal-success.component';

describe('ImportModalSuccessComponent', () => {
  let component: ImportModalSuccessComponent;
  let fixture: ComponentFixture<ImportModalSuccessComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ImportModalSuccessComponent],
    }).compileComponents();
  });

  beforeEach(() => {
    fixture = TestBed.createComponent(ImportModalSuccessComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
