import { Injectable } from '@angular/core';
import { HttpClient, HttpResponse } from '@angular/common/http';
import { Observable } from 'rxjs';
import * as moment from 'moment';
import { DATE_FORMAT } from 'app/shared/constants/input.constants';
import { map } from 'rxjs/operators';

import { SERVER_API_URL } from 'app/app.constants';
import { createRequestOption } from 'app/shared';
import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';

type EntityResponseType = HttpResponse<IHistorialDeCredito>;
type EntityArrayResponseType = HttpResponse<IHistorialDeCredito[]>;

@Injectable({ providedIn: 'root' })
export class HistorialDeCreditoService {
    public resourceUrl = SERVER_API_URL + 'api/historial-de-creditos';

    constructor(protected http: HttpClient) {}

    create(historialDeCredito: IHistorialDeCredito): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(historialDeCredito);
        return this.http
            .post<IHistorialDeCredito>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    update(historialDeCredito: IHistorialDeCredito): Observable<EntityResponseType> {
        const copy = this.convertDateFromClient(historialDeCredito);
        return this.http
            .put<IHistorialDeCredito>(this.resourceUrl, copy, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    find(id: number): Observable<EntityResponseType> {
        return this.http
            .get<IHistorialDeCredito>(`${this.resourceUrl}/${id}`, { observe: 'response' })
            .pipe(map((res: EntityResponseType) => this.convertDateFromServer(res)));
    }

    query(req?: any): Observable<EntityArrayResponseType> {
        const options = createRequestOption(req);
        return this.http
            .get<IHistorialDeCredito[]>(this.resourceUrl, { params: options, observe: 'response' })
            .pipe(map((res: EntityArrayResponseType) => this.convertDateArrayFromServer(res)));
    }

    delete(id: number): Observable<HttpResponse<any>> {
        return this.http.delete<any>(`${this.resourceUrl}/${id}`, { observe: 'response' });
    }

    protected convertDateFromClient(historialDeCredito: IHistorialDeCredito): IHistorialDeCredito {
        const copy: IHistorialDeCredito = Object.assign({}, historialDeCredito, {
            fecha:
                historialDeCredito.fecha != null && historialDeCredito.fecha.isValid() ? historialDeCredito.fecha.format(DATE_FORMAT) : null
        });
        return copy;
    }

    protected convertDateFromServer(res: EntityResponseType): EntityResponseType {
        if (res.body) {
            res.body.fecha = res.body.fecha != null ? moment(res.body.fecha) : null;
        }
        return res;
    }

    protected convertDateArrayFromServer(res: EntityArrayResponseType): EntityArrayResponseType {
        if (res.body) {
            res.body.forEach((historialDeCredito: IHistorialDeCredito) => {
                historialDeCredito.fecha = historialDeCredito.fecha != null ? moment(historialDeCredito.fecha) : null;
            });
        }
        return res;
    }
}
