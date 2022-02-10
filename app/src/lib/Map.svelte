<script lang="ts">
    import * as L from 'leaflet'
    import type {Marker} from 'leaflet'
    import MapSearch from "./MapSearch.svelte";
    import type {CancelablePromise, SearchResponseBody} from "../../api";
    import {Service} from "../../api";
    import {toast} from "@zerodevx/svelte-toast";
    import config from "../config.json"

    let map: L.Map
    let searchbar = new L.Control({position: 'topleft'})
    let mapSearch: MapSearch
    let icons: Marker[] = []
    
    searchbar.onAdd = (map: L.Map): HTMLElement => {
        const div = L.DomUtil.create('div');
        let searchResponsePromise: CancelablePromise<SearchResponseBody>
        mapSearch = new MapSearch({
            target: div,
            props: {}
        });

        mapSearch.$on("search", async ({detail}) => {
            const searchingToastId = toast.push("Searching", {duration: 10000})
            icons.forEach((icon) => icon.removeFrom(map))
            if (searchResponsePromise) { // cancel old requests
                searchResponsePromise.cancel()
            }
            searchResponsePromise = Service.getSearch(detail.text)
            searchResponsePromise.catch((error) => {
                console.log(error)
                toast.push("an error occurred: " + error)
            })

            const searchResponse = await searchResponsePromise
            toast.pop(searchingToastId)

            if (searchResponse.results.length === 0) {
                toast.push("Found no results")
            } else {
                toast.push(`Found ${searchResponse.results.length} results`)
                for (let {name, center: {latitude, longitude}} of searchResponse.results) {
                    icons.push(L.marker([latitude, longitude])
                        .addTo(map)
                        .bindTooltip(name))
                }
            }
        })

        return div
    }

    const initializeUserTracking = () => {
        let userLocation = setInterval(() => {
            navigator.geolocation.getCurrentPosition((pos) => {
                icons.push(L.marker([pos.coords.latitude, pos.coords.longitude])
                    .addTo(map));
            },
            (err) => {
                console.warn(`ERROR(${err.code}): ${err.message}`);
            },
            {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            });
        }, 2000);
        
        return () => clearInterval(userLocation)
    }

    const mapAction = (container: HTMLElement) => {
        map = L.map(container, {zoomControl: false, attributionControl: false}).setView([48.463069, -123.311833], 16);
        const stopTrackingUser = initializeUserTracking();

        L.tileLayer(
            'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
            {
                attribution: config.attribution,
                subdomains: 'abcd',
                maxZoom: 20,
            }
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
