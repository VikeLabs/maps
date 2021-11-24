<script lang="ts">
    import * as L from 'leaflet';
    import 'leaflet/dist/leaflet.css';
    import SearchBar from "./SearchBar.svelte";
    import {Service} from "../dist";

    let map: L.Map;

    function mapAction(container: HTMLElement) {
        let uvicCoords = [48.463069, -123.311833];
        map = L.map(container).setView(uvicCoords, 16);

        L.tileLayer(
            'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,&copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: 'abcd',
                maxZoom: 20,
            }
        ).addTo(map);
        toolbar.addTo(map);

        return {
            destroy: map.remove,
        };
    }

    let toolbar = L.control({position: 'topright'});

    let toolbarComponent;

    toolbar.onAdd = (map) => {
        let div = L.DomUtil.create('div');
        toolbarComponent = new SearchBar({
            target: div,
            props: {}
        });

        toolbarComponent.$on('search', async ({detail: text}) => {
            let resp = await Service.getSearch(text)
            for (let i = 0; i < resp.results.length; i++) {
                const building = resp.results[i];
                // opacity is set to zero because we don't currently bundle assets.
                L.marker([building.center.latitude, building.center.longitude], {opacity: 0})
                    .bindTooltip(building.name)
                    .addTo(map) // you have to add to map before opening
                    .openTooltip()
            }
        })

        return div;
    }

    toolbar.onRemove = () => {
        if (toolbarComponent) {
            toolbarComponent = null;
        }
    };

</script>

<div style="height:100vh;width:100vw" use:mapAction></div>
