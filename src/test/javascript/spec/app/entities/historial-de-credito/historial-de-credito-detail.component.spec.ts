/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { HistorialDeCreditoDetailComponent } from 'app/entities/historial-de-credito/historial-de-credito-detail.component';
import { HistorialDeCredito } from 'app/shared/model/historial-de-credito.model';

describe('Component Tests', () => {
    describe('HistorialDeCredito Management Detail Component', () => {
        let comp: HistorialDeCreditoDetailComponent;
        let fixture: ComponentFixture<HistorialDeCreditoDetailComponent>;
        const route = ({ data: of({ historialDeCredito: new HistorialDeCredito(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [HistorialDeCreditoDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(HistorialDeCreditoDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(HistorialDeCreditoDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.historialDeCredito).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
