/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { FacturaUpdateComponent } from 'app/entities/factura/factura-update.component';
import { FacturaService } from 'app/entities/factura/factura.service';
import { Factura } from 'app/shared/model/factura.model';

describe('Component Tests', () => {
    describe('Factura Management Update Component', () => {
        let comp: FacturaUpdateComponent;
        let fixture: ComponentFixture<FacturaUpdateComponent>;
        let service: FacturaService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [FacturaUpdateComponent]
            })
                .overrideTemplate(FacturaUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(FacturaUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(FacturaService);
        });

        describe('save', () => {
            it(
                'Should call update service on save for existing entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Factura(123);
                    spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.factura = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.update).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );

            it(
                'Should call create service on save for new entity',
                fakeAsync(() => {
                    // GIVEN
                    const entity = new Factura();
                    spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                    comp.factura = entity;
                    // WHEN
                    comp.save();
                    tick(); // simulate async

                    // THEN
                    expect(service.create).toHaveBeenCalledWith(entity);
                    expect(comp.isSaving).toEqual(false);
                })
            );
        });
    });
});
