/* tslint:disable max-line-length */
import { ComponentFixture, TestBed, inject, fakeAsync, tick } from '@angular/core/testing';
import { NgbActiveModal } from '@ng-bootstrap/ng-bootstrap';
import { Observable, of } from 'rxjs';
import { JhiEventManager } from 'ng-jhipster';

import { CreditoTestModule } from '../../../test.module';
import { AbonoDeleteDialogComponent } from 'app/entities/abono/abono-delete-dialog.component';
import { AbonoService } from 'app/entities/abono/abono.service';

describe('Component Tests', () => {
    describe('Abono Management Delete Component', () => {
        let comp: AbonoDeleteDialogComponent;
        let fixture: ComponentFixture<AbonoDeleteDialogComponent>;
        let service: AbonoService;
        let mockEventManager: any;
        let mockActiveModal: any;

        beforeEach(() => {
            TestBed.configureTestingModule({
                imports: [CreditoTestModule],
                declarations: [AbonoDeleteDialogComponent]
            })
                .overrideTemplate(AbonoDeleteDialogComponent, '')
                .compileComponents();
            fixture = TestBed.createComponent(AbonoDeleteDialogComponent);
            comp = fixture.componentInstance;
            service = fixture.debugElement.injector.get(AbonoService);
            mockEventManager = fixture.debugElement.injector.get(JhiEventManager);
            mockActiveModal = fixture.debugElement.injector.get(NgbActiveModal);
        });

        describe('confirmDelete', () => {
            it('Should call delete service on confirmDelete', inject(
                [],
                fakeAsync(() => {
                    // GIVEN
                    spyOn(service, 'delete').and.returnValue(of({}));

                    // WHEN
                    comp.confirmDelete(123);
                    tick();

                    // THEN
                    expect(service.delete).toHaveBeenCalledWith(123);
                    expect(mockActiveModal.dismissSpy).toHaveBeenCalled();
                    expect(mockEventManager.broadcastSpy).toHaveBeenCalled();
                })
            ));
        });
    });
});
