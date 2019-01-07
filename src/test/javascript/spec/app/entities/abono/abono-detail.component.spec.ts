/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { AbonoDetailComponent } from 'app/entities/abono/abono-detail.component';
import { Abono } from 'app/shared/model/abono.model';

describe('Component Tests', () => {
    describe('Abono Management Detail Component', () => {
        let comp: AbonoDetailComponent;
        let fixture: ComponentFixture<AbonoDetailComponent>;
        const route = ({ data: of({ abono: new Abono(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [AbonoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(AbonoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AbonoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.abono).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
