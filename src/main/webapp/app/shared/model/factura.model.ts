import { Moment } from 'moment';
import { ICliente } from 'app/shared/model//cliente.model';
import { IAbono } from 'app/shared/model//abono.model';

export const enum EstadoDeFactura {
    ABIERTA = 'ABIERTA',
    CANCELADA = 'CANCELADA'
}

export interface IFactura {
    id?: number;
    numeroDeFactura?: string;
    fecha?: Moment;
    total?: number;
    abonado?: number;
    estadoDeFactura?: EstadoDeFactura;
    cliente?: ICliente;
    abonos?: IAbono[];
}

export class Factura implements IFactura {
    constructor(
        public id?: number,
        public numeroDeFactura?: string,
        public fecha?: Moment,
        public total?: number,
        public abonado?: number,
        public estadoDeFactura?: EstadoDeFactura,
        public cliente?: ICliente,
        public abonos?: IAbono[]
    ) {}
}
