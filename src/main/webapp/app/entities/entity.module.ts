import { NgModule, CUSTOM_ELEMENTS_SCHEMA } from '@angular/core';

import { CreditoClienteModule } from './cliente/cliente.module';
import { CreditoFacturaModule } from './factura/factura.module';
import { CreditoAbonoModule } from './abono/abono.module';
/* jhipster-needle-add-entity-module-import - JHipster will add entity modules imports here */

@NgModule({
    // prettier-ignore
    imports: [
        CreditoClienteModule,
        CreditoFacturaModule,
        CreditoAbonoModule,
        /* jhipster-needle-add-entity-module - JHipster will add entity modules here */
    ],
    declarations: [],
    entryComponents: [],
    providers: [],
    schemas: [CUSTOM_ELEMENTS_SCHEMA]
})
export class CreditoEntityModule {}
