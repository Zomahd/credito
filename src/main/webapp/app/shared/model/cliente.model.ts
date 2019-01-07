import { IFactura } from 'app/shared/model//factura.model';

export interface ICliente {
    id?: number;
    nombre?: string;
    primerApellido?: string;
    segundoApellido?: string;
    correo?: string;
    telefono?: string;
    facturas?: IFactura[];
}

export class Cliente implements ICliente {
    constructor(
        public id?: number,
        public nombre?: string,
        public primerApellido?: string,
        public segundoApellido?: string,
        public correo?: string,
        public telefono?: string,
        public facturas?: IFactura[]
    ) {}
}
