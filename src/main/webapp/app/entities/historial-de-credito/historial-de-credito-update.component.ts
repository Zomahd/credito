import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';
import { HistorialDeCreditoService } from './historial-de-credito.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';
import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from 'app/entities/factura';
import { IAbono } from 'app/shared/model/abono.model';
import { AbonoService } from 'app/entities/abono';

@Component({
    selector: 'jhi-historial-de-credito-update',
    templateUrl: './historial-de-credito-update.component.html'
})
export class HistorialDeCreditoUpdateComponent implements OnInit {
    historialDeCredito: IHistorialDeCredito;
    isSaving: boolean;

    clientes: ICliente[];

    facturas: IFactura[];

    abonos: IAbono[];
    fechaDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected historialDeCreditoService: HistorialDeCreditoService,
        protected clienteService: ClienteService,
        protected facturaService: FacturaService,
        protected abonoService: AbonoService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ historialDeCredito }) => {
            this.historialDeCredito = historialDeCredito;
        });
        this.clienteService.query({ 'historialDeCreditoId.specified': 'false' }).subscribe(
            (res: HttpResponse<ICliente[]>) => {
                if (!this.historialDeCredito.clienteId) {
                    this.clientes = res.body;
                } else {
                    this.clienteService.find(this.historialDeCredito.clienteId).subscribe(
                        (subRes: HttpResponse<ICliente>) => {
                            this.clientes = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.facturaService.query({ 'historialDeCreditoId.specified': 'false' }).subscribe(
            (res: HttpResponse<IFactura[]>) => {
                if (!this.historialDeCredito.facturaId) {
                    this.facturas = res.body;
                } else {
                    this.facturaService.find(this.historialDeCredito.facturaId).subscribe(
                        (subRes: HttpResponse<IFactura>) => {
                            this.facturas = [subRes.body].concat(res.body);
                        },
                        (subRes: HttpErrorResponse) => this.onError(subRes.message)
                    );
                }
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
        this.abonoService.query({ 'historialDeCreditoId.specified': 'false' }).subscribe(
            (res: HttpResponse<IAbono[]>) => {
                if (!this.historialDeCredito.abonoId) {
                    this.abonos = res.body;
                } else {
                    this.abonoService.find(this.historialDeCredito.abonoId).subscribe(
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

    trackClienteById(index: number, item: ICliente) {
        return item.id;
    }

    trackFacturaById(index: number, item: IFactura) {
        return item.id;
    }

    trackAbonoById(index: number, item: IAbono) {
        return item.id;
    }
}
