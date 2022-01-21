<script lang="ts">
    import {createEventDispatcher} from "svelte";

    enum Events { Search = 'search'}

    let dispatch = createEventDispatcher()

    let text: string
</script>

<input type="text" bind:value={text} on:change={() => {
    if (text.length > 2) {
        dispatch(Events.Search, {text})
    }
}} on:dblclick={() => {
    event.stopPropagation();
    return false;
}
}>
