import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';
import { HistorialDeCreditoService } from './historial-de-credito.service';

@Component({
    selector: 'jhi-historial-de-credito-delete-dialog',
    templateUrl: './historial-de-credito-delete-dialog.component.html'
})
export class HistorialDeCreditoDeleteDialogComponent {
    historialDeCredito: IHistorialDeCredito;

    constructor(
        protected historialDeCreditoService: HistorialDeCreditoService,
        public activeModal: NgbActiveModal,
        protected eventManager: JhiEventManager
    ) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.historialDeCreditoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'historialDeCreditoListModification',
                content: 'Deleted an historialDeCredito'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-historial-de-credito-delete-popup',
    template: ''
})
export class HistorialDeCreditoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ historialDeCredito }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(HistorialDeCreditoDeleteDialogComponent as Component, {
                    size: 'lg',
                    backdrop: 'static'
                });
                this.ngbModalRef.componentInstance.historialDeCredito = historialDeCredito;
                this.ngbModalRef.result.then(
                    result => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    },
                    reason => {
                        this.router.navigate([{ outlets: { popup: null } }], { replaceUrl: true, queryParamsHandling: 'merge' });
                        this.ngbModalRef = null;
                    }
                );
            }, 0);
        });
    }

    ngOnDestroy() {
        this.ngbModalRef = null;
    }
}
