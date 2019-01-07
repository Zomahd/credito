import { Injectable } from '@angular/core';
import { HttpResponse } from '@angular/common/http';
import { Resolve, ActivatedRouteSnapshot, RouterStateSnapshot, Routes } from '@angular/router';
import { UserRouteAccessService } from 'app/core';
import { Observable, of } from 'rxjs';
import { filter, map } from 'rxjs/operators';
import { Abono } from 'app/shared/model/abono.model';
import { AbonoService } from './abono.service';
import { AbonoComponent } from './abono.component';
import { AbonoDetailComponent } from './abono-detail.component';
import { AbonoUpdateComponent } from './abono-update.component';
import { AbonoDeletePopupComponent } from './abono-delete-dialog.component';
import { IAbono } from 'app/shared/model/abono.model';

@Injectable({ providedIn: 'root' })
export class AbonoResolve implements Resolve<IAbono> {
    constructor(private service: AbonoService) {}

    resolve(route: ActivatedRouteSnapshot, state: RouterStateSnapshot): Observable<Abono> {
        const id = route.params['id'] ? route.params['id'] : null;
        if (id) {
            return this.service.find(id).pipe(
                filter((response: HttpResponse<Abono>) => response.ok),
                map((abono: HttpResponse<Abono>) => abono.body)
            );
        }
        return of(new Abono());
    }
}

export const abonoRoute: Routes = [
    {
        path: 'abono',
        component: AbonoComponent,
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.abono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'abono/:id/view',
        component: AbonoDetailComponent,
        resolve: {
            abono: AbonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.abono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'abono/new',
        component: AbonoUpdateComponent,
        resolve: {
            abono: AbonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.abono.home.title'
        },
        canActivate: [UserRouteAccessService]
    },
    {
        path: 'abono/:id/edit',
        component: AbonoUpdateComponent,
        resolve: {
            abono: AbonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.abono.home.title'
        },
        canActivate: [UserRouteAccessService]
    }
];

export const abonoPopupRoute: Routes = [
    {
        path: 'abono/:id/delete',
        component: AbonoDeletePopupComponent,
        resolve: {
            abono: AbonoResolve
        },
        data: {
            authorities: ['ROLE_USER'],
            pageTitle: 'creditoApp.abono.home.title'
        },
        canActivate: [UserRouteAccessService],
        outlet: 'popup'
    }
];
