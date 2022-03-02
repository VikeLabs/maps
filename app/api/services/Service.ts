/* istanbul ignore file */
/* tslint:disable */
/* eslint-disable */
import type { PingResponseBody } from '../models/PingResponseBody';
import type { SearchResponseBody } from '../models/SearchResponseBody';
import type { SuggestResponseBody } from '../models/SuggestResponseBody';
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

    /**
     * lists possible completions for a given string
     * Similar to search but doesn't use levenshteinDistance, instead just scanning the start of buildings
     * and their abbreviations, it is however ordered by levenshteinDistance, capitalization are ignored
     * for suggestions but included for sorting.
     * @param query The query string the user has started typing
     * @param max The max number of results returned.
     * @returns SuggestResponseBody a list of suggested search terms
     * @throws ApiError
     */
    public static getSuggest(
        query: string,
        max?: number,
    ): CancelablePromise<SuggestResponseBody> {
        return __request({
            method: 'GET',
            path: `/suggest`,
            query: {
                'query': query,
                'max': max,
            },
        });
    }

}