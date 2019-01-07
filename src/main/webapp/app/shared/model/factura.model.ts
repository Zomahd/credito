import { Moment } from 'moment';
import { ICliente } from 'app/shared/model//cliente.model';
import { IAbono } from 'app/shared/model//abono.model';

export interface IFactura {
    id?: number;
    fecha?: Moment;
    total?: number;
    abonado?: number;
    cliente?: ICliente;
    abonos?: IAbono[];
}

export class Factura implements IFactura {
    constructor(
        public id?: number,
        public fecha?: Moment,
        public total?: number,
        public abonado?: number,
        public cliente?: ICliente,
        public abonos?: IAbono[]
    ) {}
}
