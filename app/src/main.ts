import App from './App.svelte'
import 'leaflet/dist/leaflet.css'
import '../global.css'

const app = new App({
    target: document.getElementById('app')
})

export default app
