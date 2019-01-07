import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_TIME_FORMAT } from 'app/shared/constants/input.constants';
import { JhiAlertService } from 'ng-jhipster';

import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';
import { HistorialDeCreditoService } from './historial-de-credito.service';
import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from 'app/entities/factura';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';
import { IAbono } from 'app/shared/model/abono.model';
import { AbonoService } from 'app/entities/abono';

@Component({
    selector: 'jhi-historial-de-credito-update',
    templateUrl: './historial-de-credito-update.component.html'
})
export class HistorialDeCreditoUpdateComponent implements OnInit {
    historialDeCredito: IHistorialDeCredito;
    isSaving: boolean;

    facturas: IFactura[];

    clientes: ICliente[];

    abonos: IAbono[];
    startDate: string;
    endDate: string;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected historialDeCreditoService: HistorialDeCreditoService,
        protected facturaService: FacturaService,
        protected clienteService: ClienteService,
        protected abonoService: AbonoService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ historialDeCredito }) => {
            this.historialDeCredito = historialDeCredito;
            this.startDate = this.historialDeCredito.startDate != null ? this.historialDeCredito.startDate.format(DATE_TIME_FORMAT) : null;
            this.endDate = this.historialDeCredito.endDate != null ? this.historialDeCredito.endDate.format(DATE_TIME_FORMAT) : null;
        });
        this.facturaService.query({ filter: 'historialdecredito-is-null' }).subscribe(
            (res: HttpResponse<IFactura[]>) => {
                if (!this.historialDeCredito.factura || !this.historialDeCredito.factura.id) {
                    this.facturas = res.body;
                } else {
                    this.facturaService.find(this.historialDeCredito.factura.id).subscribe(
                        (subRes: HttpResponse<IFactura>) => {
                            this.facturas = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.clienteService.query({ filter: 'historialdecredito-is-null' }).subscribe(
            (res: HttpResponse<ICliente[]>) => {
                if (!this.historialDeCredito.cliente || !this.historialDeCredito.cliente.id) {
                    this.clientes = res.body;
                } else {
                    this.clienteService.find(this.historialDeCredito.cliente.id).subscribe(
                        (subRes: HttpResponse<ICliente>) => {
                            this.clientes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.abonoService.query({ filter: 'historialdecredito-is-null' }).subscribe(
            (res: HttpResponse<IAbono[]>) => {
                if (!this.historialDeCredito.abono || !this.historialDeCredito.abono.id) {
                    this.abonos = res.body;
                } else {
                    this.abonoService.find(this.historialDeCredito.abono.id).subscribe(
                        (subRes: HttpResponse<IAbono>) => {
                            this.abonos = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        this.historialDeCredito.startDate = this.startDate != null ? moment(this.startDate, DATE_TIME_FORMAT) : null;
        this.historialDeCredito.endDate = this.endDate != null ? moment(this.endDate, DATE_TIME_FORMAT) : null;
        if (this.historialDeCredito.id !== undefined) {
            this.subscribeToSaveResponse(this.historialDeCreditoService.update(this.historialDeCredito));
        } else {
            this.subscribeToSaveResponse(this.historialDeCreditoService.create(this.historialDeCredito));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IHistorialDeCredito>>) {
        result.subscribe((res: HttpResponse<IHistorialDeCredito>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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

    trackClienteById(index: number, item: ICliente) {
        return item.id;
    }

    trackAbonoById(index: number, item: IAbono) {
        return item.id;
    }
}
