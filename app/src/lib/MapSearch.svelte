<script lang="ts">
    import {createEventDispatcher} from "svelte";

    enum Events { Search = 'search'}

    let dispatch = createEventDispatcher()

    let text: string
</script>

<input type="text" bind:value={text} on:change={() => {
    dispatch(Events.Search, {text})
}}>
