import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from './factura.service';

@Component({
    selector: 'jhi-factura-delete-dialog',
    templateUrl: './factura-delete-dialog.component.html'
})
export class FacturaDeleteDialogComponent {
    factura: IFactura;

    constructor(protected facturaService: FacturaService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.facturaService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'facturaListModification',
                content: 'Deleted an factura'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-factura-delete-popup',
    template: ''
})
export class FacturaDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ factura }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(FacturaDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.factura = factura;
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
