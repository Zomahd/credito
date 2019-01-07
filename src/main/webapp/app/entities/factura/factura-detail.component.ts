import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IFactura } from 'app/shared/model/factura.model';

@Component({
    selector: 'jhi-factura-detail',
    templateUrl: './factura-detail.component.html'
})
export class FacturaDetailComponent implements OnInit {
    factura: IFactura;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ factura }) => {
            this.factura = factura;
        });
    }

    previousState() {
        window.history.back();
    }
}
