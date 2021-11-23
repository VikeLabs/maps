<script lang="ts">
    import * as L from 'leaflet';
    import 'leaflet/dist/leaflet.css';
    let map: L.Map;

    const createMap = (container: HTMLElement) => {
        let m = L.map(container).setView([51.505, -0.09], 13);
        L.tileLayer(
            'https://{s}.basemaps.cartocdn.com/rastertiles/voyager/{z}/{x}/{y}{r}.png',
            {
                attribution: `&copy;<a href="https://www.openstreetmap.org/copyright" target="_blank">OpenStreetMap</a>,
          &copy;<a href="https://carto.com/attributions" target="_blank">CARTO</a>`,
                subdomains: 'abcd',
                maxZoom: 14,
            }
        ).addTo(m);

        return m;
    };

    function mapAction(container: HTMLElement) {
        map = createMap(container);
        return {
            destroy: () => {
                map.remove();
            },
        };
    }
</script>

<div style="height:100vh;width:100vw" use:mapAction></div>
