import { Moment } from 'moment';

export interface IAbono {
    id?: number;
    fecha?: Moment;
    abono?: number;
    facturaId?: number;
}

export class Abono implements IAbono {
    constructor(public id?: number, public fecha?: Moment, public abono?: number, public facturaId?: number) {}
}
