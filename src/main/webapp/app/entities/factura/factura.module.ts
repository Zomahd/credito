import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CreditoSharedModule } from 'app/shared';
import {
    FacturaComponent,
    FacturaDetailComponent,
    FacturaUpdateComponent,
    FacturaDeletePopupComponent,
    FacturaDeleteDialogComponent,
    facturaRoute,
    facturaPopupRoute
} from './';

const ENTITY_STATES = [...facturaRoute, ...facturaPopupRoute];

@NgModule({
    imports: [CreditoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [
        FacturaComponent,
        FacturaDetailComponent,
        FacturaUpdateComponent,
        FacturaDeleteDialogComponent,
        FacturaDeletePopupComponent
    ],
    entryComponents: [FacturaComponent, FacturaUpdateComponent, FacturaDeleteDialogComponent, FacturaDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CreditoFacturaModule {}
