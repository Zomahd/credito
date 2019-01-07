import { Component, OnInit } from '@angular/core';
import { ActivatedRoute } from '@angular/router';

import { IAbono } from 'app/shared/model/abono.model';

@Component({
    selector: 'jhi-abono-detail',
    templateUrl: './abono-detail.component.html'
})
export class AbonoDetailComponent implements OnInit {
    abono: IAbono;

    constructor(protected activatedRoute: ActivatedRoute) {}

    ngOnInit() {
        this.activatedRoute.data.subscribe(({ abono }) => {
            this.abono = abono;
        });
    }

    previousState() {
        window.history.back();
    }
}
