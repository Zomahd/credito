import { Component, OnInit, OnDestroy } from '@angular/core';
import { ActivatedRoute, Router } from '@angular/router';

import { NgbActiveModal, NgbModal, NgbModalRef } from '@ng-bootstrap/ng-bootstrap';
import { JhiEventManager } from 'ng-jhipster';

import { IAbono } from 'app/shared/model/abono.model';
import { AbonoService } from './abono.service';

@Component({
    selector: 'jhi-abono-delete-dialog',
    templateUrl: './abono-delete-dialog.component.html'
})
export class AbonoDeleteDialogComponent {
    abono: IAbono;

    constructor(protected abonoService: AbonoService, public activeModal: NgbActiveModal, protected eventManager: JhiEventManager) {}

    clear() {
        this.activeModal.dismiss('cancel');
    }

    confirmDelete(id: number) {
        this.abonoService.delete(id).subscribe(response => {
            this.eventManager.broadcast({
                name: 'abonoListModification',
                content: 'Deleted an abono'
            });
            this.activeModal.dismiss(true);
        });
    }
}

@Component({
    selector: 'jhi-abono-delete-popup',
    template: ''
})
export class AbonoDeletePopupComponent implements OnInit, OnDestroy {
    protected ngbModalRef: NgbModalRef;

    constructor(protected activatedRoute: ActivatedRoute, protected router: Router, protected modalService: NgbModal) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ abono }) => {
            setTimeout(() => {
                this.ngbModalRef = this.modalService.open(AbonoDeleteDialogComponent as Component, { size: 'lg', backdrop: 'static' });
                this.ngbModalRef.componentInstance.abono = abono;
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
