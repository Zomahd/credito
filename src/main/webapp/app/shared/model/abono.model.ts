import { Moment } from 'moment';
import { IFactura } from 'app/shared/model//factura.model';

export interface IAbono {
    id?: number;
    fecha?: Moment;
    abono?: number;
    factura?: IFactura;
}

export class Abono implements IAbono {
    constructor(public id?: number, public fecha?: Moment, public abono?: number, public factura?: IFactura) {}
}
