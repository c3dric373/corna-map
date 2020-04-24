import { async, ComponentFixture, TestBed } from '@angular/core/testing';

import { MapContentComponent } from './map-content.component';
import {HttpClientModule} from '@angular/common/http';

describe('MapContentComponent', () => {
  let component: MapContentComponent;
  let fixture: ComponentFixture<MapContentComponent>;

  beforeEach(async(() => {
    TestBed.configureTestingModule({
      declarations: [ MapContentComponent ],
      imports: [HttpClientModule]
    })
    .compileComponents();
  }));

  beforeEach(() => {
    fixture = TestBed.createComponent(MapContentComponent);
    component = fixture.componentInstance;
    fixture.detectChanges();
  });

  it('should create', () => {
    expect(component).toBeTruthy();
  });
});
