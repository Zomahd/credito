import { Moment } from 'moment';
import { IFactura } from 'app/shared/model//factura.model';
import { ICliente } from 'app/shared/model//cliente.model';
import { IAbono } from 'app/shared/model//abono.model';

export interface IHistorialDeCredito {
    id?: number;
    startDate?: Moment;
    endDate?: Moment;
    factura?: IFactura;
    cliente?: ICliente;
    abono?: IAbono;
}

export class HistorialDeCredito implements IHistorialDeCredito {
    constructor(
        public id?: number,
        public startDate?: Moment,
        public endDate?: Moment,
        public factura?: IFactura,
        public cliente?: ICliente,
        public abono?: IAbono
    ) {}
}
