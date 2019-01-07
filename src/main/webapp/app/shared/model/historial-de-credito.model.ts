import { Moment } from 'moment';

export interface IHistorialDeCredito {
    id?: number;
    fecha?: Moment;
    clienteId?: number;
    facturaId?: number;
    abonoId?: number;
}

export class HistorialDeCredito implements IHistorialDeCredito {
    constructor(public id?: number, public fecha?: Moment, public clienteId?: number, public facturaId?: number, public abonoId?: number) {}
}
