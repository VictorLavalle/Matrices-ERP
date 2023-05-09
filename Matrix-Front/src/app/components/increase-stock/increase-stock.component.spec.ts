import { ComponentFixture, TestBed } from '@angular/core/testing';

import { IncreaseStockComponent } from './increase-stock.component';

describe('IncreaseStockComponent', () => {
  let component: IncreaseStockComponent;
  let fixture: ComponentFixture<IncreaseStockComponent>;

  beforeEach(async () => {
    await TestBed.configureTestingModule({
      declarations: [IncreaseStockComponent],
    }).compileComponents();

    fixture = TestBed.createComponent(IncreaseStockComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
