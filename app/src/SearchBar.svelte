<script lang="ts">
    import {createEventDispatcher} from "svelte";
    const dispatch = createEventDispatcher();
    let text = ''

    function search() {
        console.log(text)
        dispatch('search', text)
    }
</script>

<input bind:value={text} type="text" placeholder="search . . ." on:change={() => search()}>
