import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ListOfMatricesComponent } from './list-of-matrices.component';

describe('ListOfMatricesComponent', () => {
  let component: ListOfMatricesComponent;
  let fixture: ComponentFixture<ListOfMatricesComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ListOfMatricesComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ListOfMatricesComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
