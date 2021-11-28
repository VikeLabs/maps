<script lang="ts">
    import * as L from 'leaflet'
    import MapSearch from "./MapSearch.svelte";
    import {CancelablePromise, SearchResponse, Service} from "../../dist";
    import {toast} from "@zerodevx/svelte-toast";

    let map: L.Map
    let searchbar = new L.Control({position: 'topleft'})
    let mapSearch: MapSearch

    searchbar.onAdd = (map: L.Map): HTMLElement => {
        const div = L.DomUtil.create('div');
        let searchResponsePromise: CancelablePromise<SearchResponse>
        mapSearch = new MapSearch({
            target: div,
            props: {}
        });

        mapSearch.$on("search", async ({detail}) => {
            toast.push("Searching", {duration: 10000})
            if (searchResponsePromise) {
                searchResponsePromise.cancel()
            }
            searchResponsePromise = Service.getSearch(detail.text)
            searchResponsePromise.catch((error) => {
                console.log(error)
                toast.push("an error occurred: " + error)
            })

            const searchResponse = await searchResponsePromise

            if (searchResponse.results.length == 0) {
                toast.push("Found no results")
            } else {
                for (let {name, center: {latitude, longitude}} of searchResponse.results) {
                    L.marker([latitude, longitude])
                        .addTo(map)
                        .bindTooltip(name)
                }
            }
        })

        return div
    }

    const mapAction = (container: HTMLElement) => {
        map = L.map(container, {zoomControl: false, attributionControl: false}).setView([48.463069, -123.311833], 16);
        L.tileLayer(
            'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,&copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: 'abcd',
                maxZoom: 20,
            }
        ).addTo(map);
        searchbar.addTo(map)

        return {
            destroy: () => {
                map.remove()
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
