/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, fakeAsync, tick } from '@angular/core/testing';
import { HttpResponse } from '@angular/common/http';
import { Observable, of } from 'rxjs';

import { CreditoTestModule } from '../../../test.module';
import { AbonoUpdateComponent } from 'app/entities/abono/abono-update.component';
import { AbonoService } from 'app/entities/abono/abono.service';
import { Abono } from 'app/shared/model/abono.model';

describe('Component Tests', () => {
    describe('Abono Management Update Component', () => {
        let comp: AbonoUpdateComponent;
        let fixture: ComponentFixture<AbonoUpdateComponent>;
        let service: AbonoService;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [AbonoUpdateComponent]
            })
                .overrideTemplate(AbonoUpdateComponent, '')
                .compileComponents();

            fixture = TestBed.createComponent(AbonoUpdateComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AbonoService);
        });

        describe('save', () => {
            it('Should call update service on save for existing entity', fakeAsync(() => {
                // GIVEN
                const entity = new Abono(123);
                spyOn(service, 'update').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.abono = entity;
                // WHEN
                comp.save();
                tick(); // simulate async

                // THEN
                expect(service.update).toHaveBeenCalledWith(entity);
                expect(comp.isSaving).toEqual(false);
            }));

            it('Should call create service on save for new entity', fakeAsync(() => {
                // GIVEN
                const entity = new Abono();
                spyOn(service, 'create').and.returnValue(of(new HttpResponse({ body: entity })));
                comp.abono = entity;
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
