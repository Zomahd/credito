/* tslint:disable max-line-length */
import { ComponentFixture, TestBed } from '@angular/core/testing';
import { ActivatedRoute } from '@angular/router';
import { of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { FacturaDetailComponent } from 'app/entities/factura/factura-detail.component';
import { Factura } from 'app/shared/model/factura.model';

describe('Component Tests', () => {
    describe('Factura Management Detail Component', () => {
        let comp: FacturaDetailComponent;
        let fixture: ComponentFixture<FacturaDetailComponent>;
        const route = ({ data: of({ factura: new Factura(123) }) } as any) as ActivatedRoute;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [FacturaDetailComponent],
                providers: [{ provide: ActivatedRoute, useValue: route }]
            })
                .overrideTemplate(FacturaDetailComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(FacturaDetailComponent);
            comp = fixture.componentInstance;
        });

        describe('OnInit', () => {
            it('Should call load all on init', () => {
                // GIVEN

                // WHEN
                comp.ngOnInit();

                // THEN
                expect(comp.factura).toEqual(jasmine.objectContaining({ id: 123 }));
            });
        });
    });
});
