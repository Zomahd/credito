import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CreditoSharedModule } from 'app/shared';
import {
    HistorialDeCreditoComponent,
    HistorialDeCreditoDetailComponent,
    HistorialDeCreditoUpdateComponent,
    HistorialDeCreditoDeletePopupComponent,
    HistorialDeCreditoDeleteDialogComponent,
    historialDeCreditoRoute,
    historialDeCreditoPopupRoute
} from './';

const ENTITY_STATES = [...historialDeCreditoRoute, ...historialDeCreditoPopupRoute];

@NgModule({
    imports: [CreditoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        HistorialDeCreditoComponent,
        HistorialDeCreditoDetailComponent,
        HistorialDeCreditoUpdateComponent,
        HistorialDeCreditoDeleteDialogComponent,
        HistorialDeCreditoDeletePopupComponent
    ],
    entryComponents: [
        HistorialDeCreditoComponent,
        HistorialDeCreditoUpdateComponent,
        HistorialDeCreditoDeleteDialogComponent,
        HistorialDeCreditoDeletePopupComponent
    ],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CreditoHistorialDeCreditoModule {}
