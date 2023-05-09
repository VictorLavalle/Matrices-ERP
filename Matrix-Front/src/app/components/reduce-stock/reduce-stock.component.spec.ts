import { ComponentFixture, TestBed } from '@angular/core/testing';

import { ReduceStockComponent } from './reduce-stock.component';

describe('ReduceStockComponent', () => {
  let component: ReduceStockComponent;
  let fixture: ComponentFixture<ReduceStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [ReduceStockComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(ReduceStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
