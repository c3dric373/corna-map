import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { LeftSimulationComponent } from './left-simulation.component';

describe('LeftSimulationComponent', () => {
  let component: LeftSimulationComponent;
  let fixture: ComponentFixture<LeftSimulationComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ LeftSimulationComponent ]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(LeftSimulationComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
