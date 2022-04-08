<script lang="ts">
    import type {LatLngTuple, Marker} from 'leaflet'
    import * as L from 'leaflet'
    import MapSearch from "./MapSearch.svelte";
    import type {CancelablePromise, SearchResponseBody} from "../../api";
    import {Service} from "../../api";
    import {toast} from "@zerodevx/svelte-toast";
    import config from "../config.json"

    let map: L.Map
    let searchbar = new L.Control({position: 'topleft'})
    $: suggestions = [];
    let mapSearch: MapSearch
    let icons: Marker[] = []

    searchbar.onAdd = (map: L.Map): HTMLElement => {
        const div = L.DomUtil.create('div');
        let searchResponsePromise: CancelablePromise<SearchResponseBody>
        mapSearch = new MapSearch({
            target: div,
            props: {suggestions}
        });

        mapSearch.$on("search", async ({detail}) => {
            const searchingToastId = toast.push("Searching", {duration: 10000})
            while (icons.length > 0) {
                icons.pop().removeFrom(map)
            }
            if (searchResponsePromise) { // cancel old requests
                searchResponsePromise.cancel()
            }
            searchResponsePromise = Service.getSearch(detail.text)
            searchResponsePromise.catch(error => {
                console.log(error)
                toast.push("an error occurred: " + error)
            })

            const {results: buildings} = await searchResponsePromise
            toast.pop(searchingToastId)

            if (buildings.length === 0) {
                toast.push("Found no results")
            } else {
                toast.push(`Found ${buildings.length} results`)
                for (let {name, center: {latitude, longitude}} of buildings) {
                    icons.push(L.marker([latitude, longitude])
                        .addTo(map)
                        .bindTooltip(name))
                }
            }
        })

        mapSearch.$on('suggest', async ({detail}) => {
            suggestions = (await Service.getSuggest(detail.text, 5)).suggestions
            console.log(suggestions)
        })

        return div
    }

    const initializeUserTracking = () => {
        let userLocation = setInterval(() => {
            navigator.geolocation.getCurrentPosition(
                ({coords: {latitude, longitude}}) => icons.push(L.marker([latitude, longitude]).addTo(map)),
                err => console.warn(`ERROR(${err.code}): ${err.message}`),
                {
                    enableHighAccuracy: true,
                    timeout: 5000,
                    maximumAge: 0
                });
        }, 2000);

        return () => clearInterval(userLocation)
    }

    const mapAction = (container: HTMLElement) => {
        const uvic: LatLngTuple = [48.463069, -123.311833];
        map = L.map(container, {
            zoomControl: false,
            attributionControl: false,
            maxBounds: [
                [uvic[0] + 0.02, uvic[1] - 0.02],
                [uvic[0] - 0.02, uvic[1] + 0.02]
            ],
            minZoom: 15,
            maxBoundsViscosity: 0.5
        }).setView(uvic, 16);
        const stopTrackingUser = initializeUserTracking();

        L.tileLayer(
            'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
            {
                attribution: config.attribution,
            }
            // maxBounds and maxBoundsViscosity are not documented in the type definitions
            // despite being in the documentation. See https://leafletjs.com/reference.html#latlngbounds
        ).addTo(map);
        searchbar.addTo(map)

        return {
            destroy: () => {
                map.remove()
                stopTrackingUser()
            },
        };
    }
</script>

<style>
    div {
        width: 100vw;
        height: 100vh;
    }
</style>

<div use:mapAction></div>
