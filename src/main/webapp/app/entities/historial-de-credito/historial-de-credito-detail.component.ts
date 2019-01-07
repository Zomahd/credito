import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';

@Component({
    selector: 'jhi-historial-de-credito-detail',
    templateUrl: './historial-de-credito-detail.component.html'
})
export class HistorialDeCreditoDetailComponent implements OnInit {
    historialDeCredito: IHistorialDeCredito;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ historialDeCredito }) => {
            this.historialDeCredito = historialDeCredito;
        });
    }

    previousState() {
        window.history.back();
    }
}
