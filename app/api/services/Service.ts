/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { SearchResponse } from '../models/SearchResponse';
import type { Success } from '../models/Success';
import type { CancelablePromise } from '../core/CancelablePromise';
import { request as __request } from '../core/request';

export class Service {

    /**
     * Returns 200 OK and a small json object describing the server status.
     * A handy endpoint for checking to see weather the server is alive and replying.
     * @returns Success a successful ping!
     * @throws ApiError
     */
    public static getPing(): CancelablePromise<Success> {
        return __request({
            method: 'GET',
            path: `/ping`,
        });
    }

    /**
     * Route between two places.
     * @param toLat The goal latitude.
     * @param toLon The goal longitude.
     * @param fromLat The initial latitude.
     * @param fromLon The initial longitude.
     * @returns void
     * @throws ApiError
     */
    public static getRoute(
        toLat: number,
        toLon: number,
        fromLat: number,
        fromLon: number,
    ): CancelablePromise<void> {
        return __request({
            method: 'GET',
            path: `/route`,
            query: {
                'toLat': toLat,
                'toLon': toLon,
                'fromLat': fromLat,
                'fromLon': fromLon,
            },
        });
    }

    /**
     * searches the UVic campus based on a single search string
     * searches for buildings with a levenshteinDistance of 1 to the query string
     * @param query
     * @returns SearchResponse a single result
     * @throws ApiError
     */
    public static getSearch(
        query: string,
    ): CancelablePromise<SearchResponse> {
        return __request({
            method: 'GET',
            path: `/search`,
            query: {
                'query': query,
            },
        });
    }

}