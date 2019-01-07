import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { JhiPaginationUtil, JhiResolvePagingParams } from 'ng-jhipster';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { HistorialDeCredito } from 'app/shared/model/historial-de-credito.model';
import { HistorialDeCreditoService } from './historial-de-credito.service';
import { HistorialDeCreditoComponent } from './historial-de-credito.component';
import { HistorialDeCreditoDetailComponent } from './historial-de-credito-detail.component';
import { HistorialDeCreditoUpdateComponent } from './historial-de-credito-update.component';
import { HistorialDeCreditoDeletePopupComponent } from './historial-de-credito-delete-dialog.component';
import { IHistorialDeCredito } from 'app/shared/model/historial-de-credito.model';

@Injectable({ providedIn: 'root' })
export class HistorialDeCreditoResolve implements Resolve<IHistorialDeCredito> {
    constructor(private service: HistorialDeCreditoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<HistorialDeCredito> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<HistorialDeCredito>) => response.ok),
                map((historialDeCredito: HttpResponse<HistorialDeCredito>) => historialDeCredito.body)
            );
        }
        return of(new HistorialDeCredito());
    }
}

export const historialDeCreditoRoute: Routes = [
    {
        path: 'historial-de-credito',
        component: HistorialDeCreditoComponent,
        resolve: {
            pagingParams: JhiResolvePagingParams
        },
        data: {
            authorities: ['ROLE_USER'],
            defaultSort: 'id,asc',
            pageTitle: 'creditoApp.historialDeCredito.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'historial-de-credito/:id/view',
        component: HistorialDeCreditoDetailComponent,
        resolve: {
            historialDeCredito: HistorialDeCreditoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.historialDeCredito.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'historial-de-credito/new',
        component: HistorialDeCreditoUpdateComponent,
        resolve: {
            historialDeCredito: HistorialDeCreditoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.historialDeCredito.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'historial-de-credito/:id/edit',
        component: HistorialDeCreditoUpdateComponent,
        resolve: {
            historialDeCredito: HistorialDeCreditoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.historialDeCredito.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const historialDeCreditoPopupRoute: Routes = [
    {
        path: 'historial-de-credito/:id/delete',
        component: HistorialDeCreditoDeletePopupComponent,
        resolve: {
            historialDeCredito: HistorialDeCreditoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.historialDeCredito.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
