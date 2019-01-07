import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IAbono } from 'app/shared/model/abono.model';
import { AbonoService } from './abono.service';
import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from 'app/entities/factura';

@Component({
    selector: 'jhi-abono-update',
    templateUrl: './abono-update.component.html'
})
export class AbonoUpdateComponent implements OnInit {
    abono: IAbono;
    isSaving: boolean;

    facturas: IFactura[];
    fecha: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected abonoService: AbonoService,
        protected facturaService: FacturaService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ abono }) => {
            this.abono = abono;
            this.fecha = this.abono.fecha != null ? this.abono.fecha.format(DATE_TIME_FORMAT) : null;
        });
        this.facturaService.query().subscribe(
            (res: HttpResponse<IFactura[]>) => {
                this.facturas = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.abono.fecha = this.fecha != null ? moment(this.fecha, DATE_TIME_FORMAT) : null;
        if (this.abono.id !== undefined) {
            this.subscribeToSaveResponse(this.abonoService.update(this.abono));
        } else {
            this.subscribeToSaveResponse(this.abonoService.create(this.abono));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IAbono>>) {
        result.subscribe((res: HttpResponse<IAbono>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
    }

    protected onSaveSuccess() {
        this.isSaving = false;
        this.previousState();
    }

    protected onSaveError() {
        this.isSaving = false;
    }

    protected onError(errorMessage: string) {
        this.jhiAlertService.error(errorMessage, null, null);
    }

    trackFacturaById(index: number, item: IFactura) {
        return item.id;
    }
}
