/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PingResponseBody } from '../models/PingResponseBody';
import type { SearchResponseBody } from '../models/SearchResponseBody';
import type { CancelablePromise } from '../core/CancelablePromise';
import { request as __request } from '../core/request';

export class Service {

    /**
     * Returns 200 OK and a small json object describing the server status.
     * A handy endpoint for checking to see weather the server is alive and replying.
     * @returns PingResponseBody a successful ping!
     * @throws ApiError
     */
    public static getPing(): CancelablePromise<PingResponseBody> {
        return __request({
            method: 'GET',
            path: `/ping`,
        });
    }

    /**
     * searches the UVic campus based on a single search string
     * searches for buildings with a levenshteinDistance of 1 to the query string
     * @param query
     * @returns SearchResponseBody a single result
     * @throws ApiError
     */
    public static getSearch(
        query: string,
    ): CancelablePromise<SearchResponseBody> {
        return __request({
            method: 'GET',
            path: `/search`,
            query: {
                'query': query,
            },
        });
    }

}