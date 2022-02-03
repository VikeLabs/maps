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
    let longBorder = [48.470,48.457];
    let latBorder = [-123.323,-123.302];
    //let userCords = [];

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

    const getUserLocation = () => {
        setTimeout(() => {
            const mapOptions = {
                enableHighAccuracy: true,
                timeout: 5000,
                maximumAge: 0
            };

            function success(pos) {
                let crd = pos.coords;
                console.log("crd:",crd.longitude, crd.latitude);
                //userCords.push(crd.longitude, crd.latitude);
                //console.log("userCords:",userCords);
            }

            function error(err) {
                console.warn(`ERROR(${err.code}): ${err.message}`);
            }
            navigator.geolocation.getCurrentPosition(success, error, mapOptions);
        });

    }

    const mapAction = (container: HTMLElement) => {
        map = L.map(container, {zoomControl: false, attributionControl: false}).setView([48.463069, -123.311833], 16);
        getUserLocation();
        //map = L.map(container, {zoomControl: false, attributionControl: false}).setView(userCords, 16);

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
