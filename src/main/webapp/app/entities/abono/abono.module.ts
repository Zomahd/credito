import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';
import { RouterModule } from '@angular/router';

import { CreditoSharedModule } from 'app/shared';
import {
    AbonoComponent,
    AbonoDetailComponent,
    AbonoUpdateComponent,
    AbonoDeletePopupComponent,
    AbonoDeleteDialogComponent,
    abonoRoute,
    abonoPopupRoute
} from './';

const ENTITY_STATES = [...abonoRoute, ...abonoPopupRoute];

@NgModule({
    imports: [CreditoSharedModule, RouterModule.forChild(ENTITY_STATES)],
    declarations: [AbonoComponent, AbonoDetailComponent, AbonoUpdateComponent, AbonoDeleteDialogComponent, AbonoDeletePopupComponent],
    entryComponents: [AbonoComponent, AbonoUpdateComponent, AbonoDeleteDialogComponent, AbonoDeletePopupComponent],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CreditoAbonoModule {}
