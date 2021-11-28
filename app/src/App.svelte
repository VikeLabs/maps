<script lang="ts">
    import Map from "./lib/Map.svelte";
    import type {SvelteToastOptions} from "@zerodevx/svelte-toast"
    import {SvelteToast} from "@zerodevx/svelte-toast"

    const options: SvelteToastOptions = {}
</script>

<SvelteToast {options}/>
<Map/>
