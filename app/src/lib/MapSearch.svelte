<script lang="ts">
    import {createEventDispatcher} from "svelte";

    enum Events { Search = 'search', Suggest = 'suggest' }

    let dispatch = createEventDispatcher()

    let text: string
    export let suggestions: string[] = []
</script>

<input type="text" placeholder="🔍 Search UVIC..." bind:value={text} on:input={() => {
    dispatch(Events.Suggest, {text})
    if (text.length > 2) {
        dispatch(Events.Search, {text})
    }
}} on:dblclick={(event) => {
    event.stopPropagation();
}}/>

{#if (suggestions !== [])}
    <ul>
        {#each suggestions as sugg}
            <li>
                {sugg}
            </li>
        {/each}
    </ul>
{/if}


<style>
    input {
        margin-left: 2rem;
        padding-left: 1rem;
        border-radius: .5rem;
        height: 1.9rem;
        width: 20rem;
        font-size: 0.9rem;
        border: 2.6px solid rgb(0, 0, 0);
        box-shadow: 0 4px 3px rgba(126, 126, 126, 0.2);
    }

    input:active {
        border: 2.6px solid rgb(70, 139, 243);
    }

</style>

