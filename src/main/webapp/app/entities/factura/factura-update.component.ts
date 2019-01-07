import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';
import { HttpResponse, HttpErrorResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { JhiAlertService } from 'ng-jhipster';

import { IFactura } from 'app/shared/model/factura.model';
import { FacturaService } from './factura.service';
import { ICliente } from 'app/shared/model/cliente.model';
import { ClienteService } from 'app/entities/cliente';

@Component({
    selector: 'jhi-factura-update',
    templateUrl: './factura-update.component.html'
})
export class FacturaUpdateComponent implements OnInit {
    factura: IFactura;
    isSaving: boolean;

    clientes: ICliente[];
    fechaDp: any;

    constructor(
        protected jhiAlertService: JhiAlertService,
        protected facturaService: FacturaService,
        protected clienteService: ClienteService,
        protected activatedRoute: ActivatedRoute
    ) {}

    ngOnInit() {
        this.isSaving = false;
        this.activatedRoute.data.subscribe(({ factura }) => {
            this.factura = factura;
        });
        this.clienteService.query().subscribe(
            (res: HttpResponse<ICliente[]>) => {
                this.clientes = res.body;
            },
            (res: HttpErrorResponse) => this.onError(res.message)
        );
    }

    previousState() {
        window.history.back();
    }

    save() {
        this.isSaving = true;
        if (this.factura.id !== undefined) {
            this.subscribeToSaveResponse(this.facturaService.update(this.factura));
        } else {
            this.subscribeToSaveResponse(this.facturaService.create(this.factura));
        }
    }

    protected subscribeToSaveResponse(result: Observable<HttpResponse<IFactura>>) {
        result.subscribe((res: HttpResponse<IFactura>) => this.onSaveSuccess(), (res: HttpErrorResponse) => this.onSaveError());
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
}
