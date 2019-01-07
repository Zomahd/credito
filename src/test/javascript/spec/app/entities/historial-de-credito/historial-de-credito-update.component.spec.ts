/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { HistorialDeCreditoUpdateComponent } from 'app/entities/historial-de-credito/historial-de-credito-update.component';
import { HistorialDeCreditoService } from 'app/entities/historial-de-credito/historial-de-credito.service';
import { HistorialDeCredito } from 'app/shared/model/historial-de-credito.model';

describe('Component Tests', () => {
    describe('HistorialDeCredito Management Update Component', () => {
        let comp: HistorialDeCreditoUpdateComponent;
        let fixture: ComponentFixture<HistorialDeCreditoUpdateComponent>;
        let service: HistorialDeCreditoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [HistorialDeCreditoUpdateComponent]
            })
                .overrideTemplate(HistorialDeCreditoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(HistorialDeCreditoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(HistorialDeCreditoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new HistorialDeCredito(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.historialDeCredito = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new HistorialDeCredito();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.historialDeCredito = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.create).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));
        });
    });
});
